import com.google.protobuf.gradle.id

val grpcVersion: String by extra("1.70.0")
val grpcKotlinVersion: String by extra("1.4.0")
val protobufVersion: String by extra("4.30.2")

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.google.protobuf") version "0.9.5"
    id("idea")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.grpc:grpc-protobuf:${grpcVersion}")
    // Kotlinのstub実装を生成するために必要。
    implementation("io.grpc:grpc-kotlin-stub:${grpcKotlinVersion}")
    testImplementation(kotlin("test"))
}

// protoファイルの場所をsourceとして認識させる必要がある。
// 何も指定しなければデフォルトで `src/main/proto/` 配下のprotoファイルが使われるが、今回は明示的に指定している。
sourceSets {
    main {
        java {
            srcDirs("src/main/proto")
        }
    }
}

protobuf {
    // protobufスキーマのコンパイラのartifactを指定する。
    protoc {
        artifact = "com.google.protobuf:protoc:${protobufVersion}"
    }
    // stubの生成に必要なpluginを指定する。
    plugins {
        // KotlinのstubはJavaコードにも依存するため、こちらも指定する必要がある。
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${grpcKotlinVersion}:jdk8@jar"
        }
    }
    // generateProtoTasksで、上のpluginを利用する。
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

// 生成されたコードをintellij上で認識させる。
idea {
    module {
        listOf("java", "grpc", "grpckt").forEach { dir ->
            sourceSets.getByName("main").java { srcDir("$buildDir/generated/source/proto/main/$dir") }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
