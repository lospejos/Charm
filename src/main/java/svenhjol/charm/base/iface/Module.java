package svenhjol.charm.base.iface;

import svenhjol.charm.base.CharmClientModule;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Module {
    boolean alwaysEnabled() default false;

    boolean enabledByDefault() default true;

    int priority() default 0;

    String description() default "";

    String mod() default "";

    String[] limitedIfMixinsDisabled() default {};

    String[] disabledIfMixinsDisabled() default {};

    Class<? extends CharmClientModule> client() default CharmClientModule.class;
}
