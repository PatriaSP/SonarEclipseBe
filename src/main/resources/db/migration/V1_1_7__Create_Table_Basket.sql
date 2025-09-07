-- ----------------------------
-- Table structure for basket
-- ----------------------------
DROP TABLE IF EXISTS "basket" CASCADE;
CREATE TABLE "basket" (
  "id" BIGSERIAL NOT NULL,
  "user_id" int4,
  "product_id" int4,
  "qty" int4,
  "date" TIMESTAMP WITH TIME ZONE
)
;

-- ----------------------------
-- Primary Key structure for table basket
-- ----------------------------
ALTER TABLE "basket" ADD CONSTRAINT "basket_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table basket
-- ----------------------------
ALTER TABLE "basket" ADD CONSTRAINT "fk_user" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
