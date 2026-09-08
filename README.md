# gorm-event-listeners

Sample app for the apache/grails-static-website guide [GORM Event Listeners](https://grails.apache.org/guides/gorm-event-listeners/8/guide/index.html).

This branch is the Grails 8 companion for **Writing and testing GORM domain event listeners (sync + async)**: `@Subscriber` for async audit trails, `@Listener` for synchronous property updates, GORM Data Services, and Spock unit + integration tests.

## Layout

| Directory | What it is |
|---|---|
| [`initial/`](initial/) | Grails 8 web starter (`web` profile, Hibernate, Tomcat, DevTools, H2). Start here and follow the guide. |
| [`complete/`](complete/) | The same starter with `Book` / `Audit` domains, data services, async `AuditListenerService`, sync `TitleListenerService`, and tests. |

## Running

Requires JDK 21+.

```bash
cd complete
./gradlew test integrationTest
./gradlew bootRun
```

Then browse to http://localhost:8080/

## Branches

| Branch | Grails version |
|---|---|
| `grails8` | Apache Grails 8.0.0-M5 |
| `grails4` | Apache Grails 4 (published guide) |

## Guide prose

Published narrative lives on [grails.apache.org/guides](https://grails.apache.org/guides/) in [apache/grails-static-website](https://github.com/apache/grails-static-website) under `guides/gorm-event-listeners/`.

## License

Apache License 2.0. See [LICENSE](LICENSE).
