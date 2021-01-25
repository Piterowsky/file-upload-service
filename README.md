# Cars file upload
### Spring project handling upload file with cars records and saving it to database

Server exposes one POST two endpoints available at
- `/api/upload`
- `/api/upload/{car_color}`

Command to run application `./mvnw spring-boot:run`

## Extending application for other file types
At the moment server handle CSV file but is prepared to be extended with other file types. It just need to add new implementation of `FileParser` interface which handle parsing file content to list of key pair values [column_header -> value] and to add spring bean to switch [here](https://github.com/Piterowsky/samochody/blob/master/src/main/java/pl/piterowsky/cars/facade/CarFacade.java).

## Extending application for other model entities
Simillary to extending supported file types it is possible to extend application for new entities. To do that need to be provided implementation of `FileConversionService` with new model class.

## Sample file
In test there is a [sample of CSV file](https://github.com/Piterowsky/samochody/blob/master/src/test/resources/cars.csv) which can be used as a template for demonstration purposes.