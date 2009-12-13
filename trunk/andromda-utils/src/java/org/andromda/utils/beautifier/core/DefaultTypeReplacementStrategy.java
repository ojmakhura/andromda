package org.andromda.utils.beautifier.core;

/**
 * Copyright 2008 hybrid labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Default implementation of the TypeReplacement strategy.
 *
 * @author Karsten Klein, hybrid labs; Plushnikov Michail
 */
public class DefaultTypeReplacementStrategy implements TypeReplacementStrategy {

    private String suffix;

    public DefaultTypeReplacementStrategy(String suffix) {
        this.suffix = '(' + suffix + ')';
    }

    public String composeMatch(String type) {
        return type.replaceAll("\\.", "\\\\s*\\.\\\\s*") + suffix;
    }

    public String modulateType(String type) {
        return type;
    }

    public String composeReplace(String type) {
        String t = type.substring(type.lastIndexOf('.') + 1);
        return t + "$1";
    }
}
