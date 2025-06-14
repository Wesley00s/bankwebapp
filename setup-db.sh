#!/bin/bash

HOST="192.168.56.10"
DB_NAME="bank_db"
USER="bank"
PASSWORD="banksecret"
SQL_FILE="src/main/resources/create.sql"

mysql -h $HOST -u $USER -p$PASSWORD <<EOF
CREATE DATABASE IF NOT EXISTS \`$DB_NAME\`;
USE \`$DB_NAME\`;
SOURCE $SQL_FILE;
EOF

if [ $? -eq 0 ]; then
    echo "Banco de dados e tabelas criados com sucesso! ;)"
else
    echo "Erro ao executar o script SQL :("
    exit 1
fi