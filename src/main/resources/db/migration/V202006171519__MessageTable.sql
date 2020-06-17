CREATE TABLE Message (
    id              BIGSERIAL PRIMARY KEY,
    message_value   VARCHAR(200) NOT NULL,
    sender          VARCHAR(200) NOT NULL,
    send_date       TIMESTAMP    NOT NULL,
    message_type    VARCHAR(50)  NOT NULL,
    chat_id         BIGINT       NOT NULL REFERENCES chat(id)
)