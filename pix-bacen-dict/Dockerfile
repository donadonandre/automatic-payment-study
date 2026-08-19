# --- build stage ---
FROM golang:1.26-alpine AS builder

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o dict-service ./cmd/dict

# --- runtime stage ---
FROM alpine:3.20

RUN apk add --no-cache ca-certificates

WORKDIR /app
COPY --from=builder /app/dict-service .

EXPOSE 9090
ENTRYPOINT ["./dict-service"]