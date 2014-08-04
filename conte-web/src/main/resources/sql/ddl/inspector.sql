ALTER TABLE `sgm_conte`.`inspector` 
ADD COLUMN `estado_inspector_id` TINYINT NULL COMMENT 'Establece el estado de un inspector 1 activo, 2 inactivo' 
AFTER `email_personal`;