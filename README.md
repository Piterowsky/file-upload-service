# File upload service

Application takes file (csv/xls/xlsx) with records of some entity (at this moment it only support cars - look at "Sample file" section), and saves it into in memory database

> It is just a showcase application, not production ready software, so please have it in mind :wink:

Server exposes one POST two endpoints available at
- `/api/upload`
- `/api/upload/{car_color}`

Command to run application `docker-compose up --build`

Web form for uploading file is accessible from `http://localhost`

## Extending application for other file types
To add new file extension it's just need to add new implementation of `FileParser` interface which handle parsing file content to list of key pair values `[columnHeader -> value]` and to add spring bean to switch [here](https://github.com/Piterowsky/samochody/blob/master/src/main/java/pl/piterowsky/cars/facade/CarFacade.java).

## Extending application for other model entities
Simillary to extending supported file types it is possible to extend application for new entities. To do that need to be provided implementation of `FileConversionService` with new model class.

## Sample file
In test there is a 
- [sample of CSV file](https://github.com/Piterowsky/samochody/blob/master/src/test/resources/cars.csv)
- [sample of XLS file](https://github.com/Piterowsky/samochody/blob/master/src/test/resources/cars.xls)
- [sample of XLSX file](https://github.com/Piterowsky/samochody/blob/master/src/test/resources/cars.xlsx)

which can be used as a template for demonstration purposes.

## Interesting classes
[FileParserTest](https://github.com/Piterowsky/file-upload-service/blob/master/src/test/java/pl/piterowsky/cars/file/parser/impl/FileParserTest.java) - Interesting example of use junit5 parametrized tests
