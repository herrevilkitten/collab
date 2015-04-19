package org.evilkitten.gitboard.application.services.whiteboard.shape;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import de.danielbechler.diff.access.PropertyAwareAccessor;
import de.danielbechler.diff.instantiation.TypeInfo;
import de.danielbechler.diff.introspection.PropertyAccessor;
import de.danielbechler.diff.introspection.StandardIntrospector;
import de.danielbechler.util.Assert;
import de.danielbechler.util.Exceptions;

public class ShapeIntrospector extends StandardIntrospector {
    public TypeInfo introspect(final Class<?> type) {
        Assert.notNull(type, "type");
        try {
            return internalIntrospect(type);
        } catch (final IntrospectionException e) {
            throw Exceptions.escalate(e);
        }
    }

    private TypeInfo internalIntrospect(final Class<?> type) throws IntrospectionException {
        final TypeInfo typeInfo = new TypeInfo(type);
        final PropertyDescriptor[] descriptors = getBeanInfo(type).getPropertyDescriptors();
        for (final PropertyDescriptor descriptor : descriptors) {
            if (shouldSkip(descriptor)) {
                continue;
            }
            final String propertyName = descriptor.getName();
            final Method readMethod = descriptor.getReadMethod();
            final Method writeMethod = descriptor.getWriteMethod();
            final PropertyAwareAccessor accessor = new PropertyAccessor(propertyName, readMethod, writeMethod);
            typeInfo.addPropertyAccessor(accessor);
        }
        return typeInfo;
    }

    protected BeanInfo getBeanInfo(final Class<?> type) throws IntrospectionException {
        return Introspector.getBeanInfo(type);
    }

    private static boolean shouldSkip(final PropertyDescriptor descriptor) {
        if (descriptor.getName().equals("class")) // Java & Groovy
        {
            return true;
        }
        if (descriptor.getName().equals("metaClass")) // Groovy
        {
            return true;
        }
        if (descriptor.getName().equals("id")) {
            return true;
        }
        if (descriptor.getName().equals("creationTime")) {
            return true;
        }
        if (descriptor.getName().equals("name")) {
            return true;
        }
        if (descriptor.getName().equals("originalId")) {
            return true;
        }
        if (descriptor.getName().equals("parentId")) {
            return true;
        }
        if (descriptor.getReadMethod() == null) {
            return true;
        }
        return false;
    }
}
