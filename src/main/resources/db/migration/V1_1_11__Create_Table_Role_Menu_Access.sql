-- ----------------------------
-- Table structure for role_menu_access
-- ----------------------------
DROP TABLE IF EXISTS "role_menu_access" CASCADE;
CREATE TABLE "role_menu_access" (
  "id" BIGSERIAL NOT NULL,
  "menu_id" int4,
  "role_id" int4
)
;

-- ----------------------------
-- Primary Key structure for table role_menu_access
-- ----------------------------
ALTER TABLE "role_menu_access" ADD CONSTRAINT "role_menu_access_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table role_menu_access
-- ----------------------------
ALTER TABLE "role_menu_access" ADD CONSTRAINT "fk_menu" FOREIGN KEY ("menu_id") REFERENCES "menu" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "role_menu_access" ADD CONSTRAINT "fk_role" FOREIGN KEY ("role_id") REFERENCES "roles" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
