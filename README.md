# Your's Form 

Dynamic form builder that allows you to create forms with different types of questions such as short answer, paragraph, date input, multiple choice, dropdown and checkboxes.

### Migration 

Requirements:
- Java 17

Run the following command to create the database schema:
```bash
gradle flywayMigrate -Dflyway.url=jdbc:postgresql://localhost:5432/your_form -Dflyway.user=root -Dflyway.password=root
```

```

