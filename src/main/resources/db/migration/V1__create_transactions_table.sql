CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    description varchar(50) NOT NULL,
    transaction_date DATE NOT NULL,
    amount NUMERIC(18,2) NOT NULL
);