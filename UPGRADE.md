# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*Por ejemplo, si tuvieramos una instalación en la versión 8.1.2 y quisieramos actualizar a la X.Y.Z, tenemos que ejecutar primero, el proceso de actualización* **8.2.2 a 8.2.3** *y a continuación,* **8.2.3 a X.Y.Z .**

## 8.2.3 a X.Y.Z
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.3/db](etc/changes-from-release/8.2.3/db)
* Actualizar el WAR

## 8.2.2 a 8.2.3
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.2/db](etc/changes-from-release/8.2.2/db)
* Actualizar el WAR

## 0.0.0 a 8.1.1
* El proceso de actualizaciones entre versiones para versiones anteriores a la 8.1.1 está definido en "Metamac - Manual de instalación.doc"
