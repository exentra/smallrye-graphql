/*
 * Copyright 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.smallrye.graphql.bootstrap;

import org.jboss.jandex.Type;

import io.smallrye.graphql.bootstrap.schema.ArgumentTypeNotFoundException;

/**
 * Simple Argument Holder that contains meta data about an argument
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class Argument {
    private String name;
    private Type type;
    private Annotations annotations;

    public Argument() {
        super();
    }

    public Argument(String name, Type type, Annotations annotations) {
        super();
        this.name = name;
        this.type = type;
        this.annotations = annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public Class getArgumentClass() {
        Type.Kind kind = type.kind();
        String typename = type.name().toString();
        if (kind.equals(Type.Kind.PRIMITIVE)) {
            return Classes.getPrimativeClassType(typename);
        } else {
            try {
                return Class.forName(typename);
            } catch (ClassNotFoundException ex) {
                throw new ArgumentTypeNotFoundException(ex);
            }
        }
    }

}
