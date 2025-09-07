-- ----------------------------
-- Table structure for users_address
-- ----------------------------
DROP TABLE IF EXISTS "users_address" CASCADE;
CREATE TABLE "users_address" (
  "id" BIGSERIAL NOT NULL,
  "address" text COLLATE "pg_catalog"."default",
  "user_id" int4
)
;

-- ----------------------------
-- Primary Key structure for table users_address
-- ----------------------------
ALTER TABLE "users_address" ADD CONSTRAINT "user_address_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table users_address
-- ----------------------------
ALTER TABLE "users_address" ADD CONSTRAINT "fk_user" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
