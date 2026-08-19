package main

import (
	"database/sql"
	"encoding/json"
	"errors"
	"log"
	"net/http"

	_ "modernc.org/sqlite"
)

type PixKey struct {
	KeyValue        string `json:"keyValue"`
	KeyType         string `json:"keyType"`
	ParticipantISPB string `json:"participantIspb"`
	AccountRef      string `json:"accountRef"`
}

var db *sql.DB

func main() {
	var err error
	db, err = sql.Open("sqlite", "dict.db")
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	if _, err := db.Exec(schemaSQL); err != nil {
		log.Fatal(err)
	}

	mux := http.NewServeMux()
	mux.HandleFunc("GET /keys/{value}", handleGetKey)
	mux.HandleFunc("POST /keys", handleRegisterKey)
	mux.HandleFunc("DELETE /keys/{value}", handleDeleteKey)

	log.Println("dict-service listening on :9090")
	log.Fatal(http.ListenAndServe(":9090", mux))
}

func handleGetKey(w http.ResponseWriter, r *http.Request) {
	value := r.PathValue("value")

	var k PixKey
	err := db.QueryRow(
		"SELECT key_value, key_type, participant_ispb, account_ref FROM pix_keys WHERE key_value = ?",
		value,
	).Scan(&k.KeyValue, &k.KeyType, &k.ParticipantISPB, &k.AccountRef)

	if errors.Is(err, sql.ErrNoRows) {
		http.Error(w, `{"error":"pix key not found"}`, http.StatusNotFound)
		return
	}
	if err != nil {
		http.Error(w, `{"error":"internal error"}`, http.StatusInternalServerError)
		return
	}

	writeJSON(w, http.StatusOK, k)
}

func handleRegisterKey(w http.ResponseWriter, r *http.Request) {
	var k PixKey
	if err := json.NewDecoder(r.Body).Decode(&k); err != nil {
		http.Error(w, `{"error":"invalid body"}`, http.StatusBadRequest)
		return
	}

	if k.KeyType == "RANDOM" && k.KeyValue == "" {
		k.KeyValue = generateRandomKey()
	}

	_, err := db.Exec(
		"INSERT INTO pix_keys (key_value, key_type, participant_ispb, account_ref) VALUES (?, ?, ?, ?)",
		k.KeyValue, k.KeyType, k.ParticipantISPB, k.AccountRef,
	)
	if err != nil {
		// unique constraint violado -> chave já registrada em algum participante
		http.Error(w, `{"error":"key already registered"}`, http.StatusConflict)
		return
	}

	writeJSON(w, http.StatusCreated, k)
}

func handleDeleteKey(w http.ResponseWriter, r *http.Request) {
	value := r.PathValue("value")

	result, err := db.Exec("DELETE FROM pix_keys WHERE key_value = ?", value)
	if err != nil {
		http.Error(w, `{"error":"internal error"}`, http.StatusInternalServerError)
		return
	}

	rows, _ := result.RowsAffected()
	if rows == 0 {
		http.Error(w, `{"error":"pix key not found"}`, http.StatusNotFound)
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(v)
}
