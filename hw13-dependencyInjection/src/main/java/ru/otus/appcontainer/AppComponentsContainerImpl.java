package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> annotatedMethods = getAnnotatedMethods(configClass);
        List<Method> sortedMethods = getSortedMethods(annotatedMethods);

        Object configClassInstance = createInstanceWithDefaultConstructor(configClass);
        for (Method method : sortedMethods) {
            checkDuplicateKey(method);

            Object[] args = getArgsForMethod(method);
            Object componentInstance = invokeMethodForCreatingBean(method, configClassInstance, args);

            appComponents.add(componentInstance);
            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), componentInstance);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Method> getAnnotatedMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.getAnnotation(AppComponent.class) != null).toList();
    }

    private List<Method> getSortedMethods(List<Method> annotatedMethods) {
        return annotatedMethods.stream().sorted(Comparator.comparingInt(o -> o.getAnnotation(AppComponent.class).order())).toList();
    }

    private Object createInstanceWithDefaultConstructor(Class<?> clazz) {
        Object res;
        try {
            res = clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Error happened while creating instance", e);
        }
        return res;
    }

    private Object invokeMethodForCreatingBean(Method method, Object instance, Object[] args) {
        Object bean;
        try {
            bean = method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error happened while creating object", e);
        }
        return bean;
    }

    private void checkDuplicateKey(Method method) {
        if (appComponentsByName.containsKey(method.getAnnotation(AppComponent.class).name())) {
            throw new RuntimeException(String
                    .format("Bean with name %s already exists", method.getAnnotation(AppComponent.class).name()));
        }
    }

    private Object[] getArgsForMethod(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = appComponents.stream()
                    .filter(parameters[i]::isInstance).findFirst()
                    .orElseThrow(() -> new RuntimeException("Unable to find component"));
        }
        return args;
    }

    private void checkDuplicateClass(Class<?> componentClass) {
        List<Object> duplicates = appComponents.stream()
                .filter(componentClass::isInstance).toList();
        if (duplicates.size() > 1) {
            throw new RuntimeException(String.format("There are more than one bean of this class %s", componentClass.getSimpleName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        checkDuplicateClass(componentClass);
        return (C) appComponents.stream()
                .filter(componentClass::isInstance).findFirst()
                .orElseThrow(() -> new RuntimeException("Unable to find component"));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) Optional.ofNullable(appComponentsByName.get(componentName))
                .orElseThrow(() -> new RuntimeException("Unable to find component"));
    }
}
