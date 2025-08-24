plugins { `kotlin-dsl` }


java {
    // JDK für die Build-Logic selbst (hat nichts mit Projekt-Toolchain zu tun)
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
}
kotlin { jvmToolchain(21) }

