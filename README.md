Spring 5 to Spring 6 migration

1. Updating Dependencies
In your pom.xml, update the Spring dependencies to the latest version:

 pom.xml for Spring 5
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>spring5-example</artifactId>
    <version>1.0.0</version>
    <properties>
        <java.version>8</java.version>
        <spring.version>5.3.20</spring.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- Add other dependencies as needed -->
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
 pom.xml for Spring 6
 ```xml 
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>spring6-example</artifactId>
    <version>1.0.0</version>
    <properties>
        <java.version>17</java.version>
        <spring.version>6.0.11</spring.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>${spring.version}</version>
        </dependency>
        <!-- Add other dependencies as needed -->
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
2. Java Version
Ensure your project is using Java 17 or higher:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```
3. Namespace Changes
Update your imports from javax to jakarta:
```java
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
```
4. Configuration Changes
For XML-based configuration, update schema references:

Spring 5 XML Configuration:
```xml
______________________________________________________________________
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- Your bean definitions -->
</beans>
```
_____________________________________________________________________________
Spring 6 XML Configuration: its always use https instead of http
```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           https://www.springframework.org/schema/beans/spring-beans.xsd">
    <!-- Your bean definitions -->
</beans>
```
__________________________________________________________________________

For Java-based configuration, replace deprecated methods:
```java
@Configuration
public class AppConfig {
    @Bean
    public SomeService someService() {
        return new SomeServiceImpl();
    }
}
```
5. Security Configuration
Replace WebSecurityConfigurerAdapter with SecurityFilterChain:
```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults());
        return http.build();
    }
}
```
6. Authorization Changes
Use authorizeHttpRequests() instead of authorizeRequests():
```java
http
    .authorizeHttpRequests(authz -> authz
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
    );
```
7. Functional Style Configuration
Adopt functional style for methods like cors() and csrf():
```java
http
    .cors(cors -> cors.disable())
    .csrf(csrf -> csrf.disable());
```
