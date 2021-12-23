#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER ebooks;
    CREATE DATABASE ebooks;
    GRANT ALL PRIVILEGES ON DATABASE ebooks TO ebooks;
EOSQL
