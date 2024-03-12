package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.internal.artifacts.dependencies.ProjectDependencyInternal;
import org.gradle.api.internal.artifacts.DefaultProjectDependencyFactory;
import org.gradle.api.internal.artifacts.dsl.dependencies.ProjectFinder;
import org.gradle.api.internal.catalog.DelegatingProjectDependency;
import org.gradle.api.internal.catalog.TypeSafeProjectDependencyFactory;
import javax.inject.Inject;

@NonNullApi
public class RootProjectAccessor extends TypeSafeProjectDependencyFactory {


    @Inject
    public RootProjectAccessor(DefaultProjectDependencyFactory factory, ProjectFinder finder) {
        super(factory, finder);
    }

    /**
     * Creates a project dependency on the project at path ":"
     */
    public HealthAndroidProjectDependency getHealthAndroid() { return new HealthAndroidProjectDependency(getFactory(), create(":")); }

    /**
     * Creates a project dependency on the project at path ":app"
     */
    public AppProjectDependency getApp() { return new AppProjectDependency(getFactory(), create(":app")); }

    /**
     * Creates a project dependency on the project at path ":common-ui"
     */
    public CommonUiProjectDependency getCommonUi() { return new CommonUiProjectDependency(getFactory(), create(":common-ui")); }

    /**
     * Creates a project dependency on the project at path ":lint"
     */
    public LintProjectDependency getLint() { return new LintProjectDependency(getFactory(), create(":lint")); }

    /**
     * Creates a project dependency on the project at path ":mobius-base"
     */
    public MobiusBaseProjectDependency getMobiusBase() { return new MobiusBaseProjectDependency(getFactory(), create(":mobius-base")); }

    /**
     * Creates a project dependency on the project at path ":mobius-migration"
     */
    public MobiusMigrationProjectDependency getMobiusMigration() { return new MobiusMigrationProjectDependency(getFactory(), create(":mobius-migration")); }

    /**
     * Creates a project dependency on the project at path ":sharedTestCode"
     */
    public SharedTestCodeProjectDependency getSharedTestCode() { return new SharedTestCodeProjectDependency(getFactory(), create(":sharedTestCode")); }

    /**
     * Creates a project dependency on the project at path ":simple-platform"
     */
    public SimplePlatformProjectDependency getSimplePlatform() { return new SimplePlatformProjectDependency(getFactory(), create(":simple-platform")); }

    /**
     * Creates a project dependency on the project at path ":simple-visuals"
     */
    public SimpleVisualsProjectDependency getSimpleVisuals() { return new SimpleVisualsProjectDependency(getFactory(), create(":simple-visuals")); }

}
