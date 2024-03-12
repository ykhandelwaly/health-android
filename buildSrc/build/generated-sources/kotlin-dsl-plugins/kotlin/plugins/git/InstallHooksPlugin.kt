package plugins.git


/**
 * Precompiled [install-hooks.gradle.kts][plugins.git.Install_hooks_gradle] script plugin.
 *
 * @see plugins.git.Install_hooks_gradle
 */
public
class InstallHooksPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("plugins.git.Install_hooks_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
