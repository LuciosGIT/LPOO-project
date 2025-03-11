plugins {
    java
    application
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    // libGDX Core
    implementation("com.badlogicgames.gdx:gdx:1.12.0")

    // Backend Desktop (LWJGL3)
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.0")

    // Dependências Nativas para Desktop
    implementation("com.badlogicgames.gdx:gdx-platform:1.12.0:natives-desktop")

    // (Opcional) Biblioteca para Física (Box2D)
    implementation("com.badlogicgames.gdx:gdx-box2d:1.12.0")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:1.12.0:natives-desktop")
}

application {
    mainClass.set("com.mygdx.game.DesktopLauncher")
}

tasks.withType<JavaExec> {
    // Para evitar erros de reflexão no Java 21
    jvmArgs = listOf("--add-opens", "java.base/java.lang=ALL-UNNAMED")
}