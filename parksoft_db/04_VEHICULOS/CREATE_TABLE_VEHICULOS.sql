CREATE TABLE `parksoft`.`vehiculos` (
  `ID_VEHICULO` INT NOT NULL AUTO_INCREMENT,
  `PLACA` VARCHAR(6) NOT NULL,
  `HORA_INGRESO` TIMESTAMP,
  `HORA_SALIDA` TIMESTAMP,
  `ID_PARKING` INT,
  `ESTADO` VARCHAR(2),
  PRIMARY KEY (`ID_VEHICULO`),
  CONSTRAINT `FK_VEHICULOS_ID_PARKING`
    FOREIGN KEY (`ID_PARKING`)
    REFERENCES `parksoft`.`parkings` (`ID_PARKING`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
  );
  
ALTER TABLE `parksoft`.`vehiculos`
ADD CONSTRAINT `CHK_ESTADO` CHECK (`ESTADO` IN ('P', 'NP'));