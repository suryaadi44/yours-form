# Your's Form 

Dynamic form builder that allows you to create forms with different types of questions such as short answer, paragraph, date input, multiple choice, dropdown and checkboxes.

### Migration 

Requirements:
- Java 17
- Liquibase (Follow the instructions in the [official documentation](https://docs.liquibase.com/start/install/home.html) for installation)

Run the following command to apply the migrations:
```bash
liquibase --changeLogFile=src/main/resources/db/changelog/db.changelog-master.sql --url=jdbc:postgresql://localhost:5432/your_form --username=your_form --password=your_form update
```

