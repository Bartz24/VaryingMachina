package com.bartz24.varyingmachina.base.recipe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ProcessRecipeRegistry {
    public static enum ProcessRecipeStage
    {
        INIT,
        POSTINIT
    }

    public ProcessRecipeStage value() default ProcessRecipeStage.INIT;
}
