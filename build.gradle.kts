import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(17)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    group = "ru.otus"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val testcontainersBom: String by project
    val protobufBom: String by project
    val guava: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.springframework.boot:spring-boot-starter:3.2.1")
            dependency("com.google.guava:guava:33.0.0-jre")
            dependency("org.springframework.boot:spring-boot-starter-test:3.2.1")
            dependency("org.assertj:assertj-core:3.25.3")
            dependency("org.mockito:mockito-core:5.10.0")
            dependency("com.google.code.gson:gson:2.10.1")
            dependency("ch.qos.logback:logback-classic:1.4.14")
            dependency("org.flywaydb:flyway-core:9.22.3")
            dependency("com.zaxxer:HikariCP:5.1.0")
            dependency("org.postgresql:postgresql:42.7.3")
            dependency("org.projectlombok:lombok:1.18.32")
            dependency("com.h2database:h2:2.2.224")
            dependency("org.testcontainers:junit-jupiter:1.19.7")
            dependency("org.testcontainers:postgresql:1.19.7")
            dependency("org.freemarker:freemarker:2.3.31")
            dependency("org.eclipse.jetty:jetty-server:12.0.8")
            dependency("org.eclipse.jetty:jetty-security:12.0.8")
            dependency("org.eclipse.jetty:jetty-http:12.0.8")
            dependency("org.eclipse.jetty:jetty-io:12.0.8")
            dependency("org.eclipse.jetty:jetty-util:12.0.8")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.0.8")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-webapp:12.0.8")
            dependency("org.springframework.boot:spring-boot-starter-data-jdbc:3.2.5")
            dependency("org.springframework.boot:spring-boot-starter-web:3.2.5")
            dependency("org.springframework.boot:spring-boot-starter-thymeleaf:3.2.5")
            dependency("io.grpc:grpc-netty:1.64.0")
            dependency("io.grpc:grpc-protobuf:1.64.0")
            dependency("io.grpc:grpc-stub:1.64.0")
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            force("javax.servlet:servlet-api:2.4")
            force("commons-logging:commons-logging:1.1.1")
            force("commons-lang:commons-lang:2.5")
            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
            force("org.codehaus.jackson:jackson-mapper-asl:1.8.3")
            force("org.codehaus.jettison:jettison:1.1")
            force("net.java.dev.jna:jna:5.8.0")
            force("com.google.errorprone:error_prone_annotations:2.7.1")
            force("org.sonarsource.analyzer-commons:sonar-analyzer-commons:2.3.0.1263")
            force("com.google.code.findbugs:jsr305:3.0.2")
            force("org.sonarsource.sslr:sslr-core:1.24.0.633")
            force("org.eclipse.platform:org.eclipse.osgi:3.18.400")
            force("org.eclipse.platform:org.eclipse.equinox.common:3.18.0")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }
}

tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}