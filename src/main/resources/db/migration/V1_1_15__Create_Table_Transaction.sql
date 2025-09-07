-- ----------------------------
-- Table structure for transactions
-- ----------------------------
DROP TABLE IF EXISTS "transactions" CASCADE;
CREATE TABLE "transactions" (
  "id" BIGSERIAL NOT NULL,
  "user_id" int4,
  "invoice_num" varchar(255),
  "product_id" int4,
  "payment_id" int4,
  "address_id" int4,
  "expedition_id" int4,
  "qty" int4,
  "total" int8,
  "created_by"  BIGINT       NOT NULL    DEFAULT 0,
  "created_at"  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  "updated_by"  BIGINT       NULL,
  "updated_at"  TIMESTAMP WITH TIME ZONE,
  "deleted_by"  BIGINT       NULL,
  "deleted_at"  TIMESTAMP WITH TIME ZONE,
  "status" varchar(50)
)
;

-- ----------------------------
-- Primary Key structure for table transactions
-- ----------------------------
ALTER TABLE "transactions" ADD CONSTRAINT "product_purcashed_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table transactions
-- ----------------------------
ALTER TABLE "transactions" ADD CONSTRAINT "fk_address" FOREIGN KEY ("address_id") REFERENCES "users_address" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "transactions" ADD CONSTRAINT "fk_expedition" FOREIGN KEY ("expedition_id") REFERENCES "expedition" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "transactions" ADD CONSTRAINT "fk_payment" FOREIGN KEY ("payment_id") REFERENCES "payment" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "transactions" ADD CONSTRAINT "fk_product" FOREIGN KEY ("product_id") REFERENCES "product" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "transactions" ADD CONSTRAINT "fk_users" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "expedition_history" ADD CONSTRAINT "fk_transaction" FOREIGN KEY ("transaction_id") REFERENCES "transactions" ("id") ON DELETE SET NULL ON UPDATE CASCADE;
ALTER TABLE "product_review" ADD CONSTRAINT "fk_transactions_id" FOREIGN KEY ("transactions_id") REFERENCES "transactions" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
