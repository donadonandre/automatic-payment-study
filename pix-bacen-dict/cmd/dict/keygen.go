package main

import (
	"crypto/rand"
	"fmt"
)

// generateRandomKey produces a UUID v4, exactly the format the real
// Pix "EVP" (chave aleatória) uses — RFC 4122, version 4, variant 10xx.
func generateRandomKey() string {
	b := make([]byte, 16)
	if _, err := rand.Read(b); err != nil {
		panic(err) // crypto/rand failing is an unrecoverable environment error
	}

	b[6] = (b[6] & 0x0f) | 0x40 // version 4
	b[8] = (b[8] & 0x3f) | 0x80 // variant 10xx

	return fmt.Sprintf("%x-%x-%x-%x-%x", b[0:4], b[4:6], b[6:8], b[8:10], b[10:16])
}
