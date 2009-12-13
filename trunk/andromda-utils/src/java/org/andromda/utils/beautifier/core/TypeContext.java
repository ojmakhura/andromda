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

import java.util.List;

/**
 * The type context enable some convenient operation on a type extracted from
 * an abstract systax tree.
 *
 * @author Karsten Klein, hybrid labs; Plushnikov Michail
 */
public class TypeContext implements Comparable {

    private int type;
    private List<String> sequence;

    private String qualifiedName = null;

    public TypeContext(int type, List<String> sequence) {
        this.type = type;
        this.sequence = sequence;
    }

    public String getQualifiedName() {
        if (qualifiedName == null) {
            StringBuilder sb = new StringBuilder(100);
            for (String lSequence : sequence) {
                if (sb.length() > 0) {
                    sb.append('.');
                }
                sb.append(lSequence);
            }
            qualifiedName = sb.toString();
        }
        return qualifiedName;
    }

    public int getType() {
        return type;
    }

    public String toString() {
        return String.valueOf(getType()) + " - " + getQualifiedName();
    }

    public void reviseType() {
        int removeIndex = -1;

        if (type == 50) return;
        if (type == 19) return;
        if (type == 55) return; // package

        for (int i = 0; i < sequence.size(); i++) {
            String pos = sequence.get(i);
            
            // uses java conventions to revise type
            if (pos.matches("[A-Z].*")) {
                removeIndex = i + 1;
                break;
            }
        }

        if (removeIndex != -1 && removeIndex < sequence.size()) {
            while (sequence.size() != removeIndex) {
                sequence.remove(removeIndex);
                qualifiedName = null;
            }
        }
    }

    public int compareTo(Object arg0) {
        TypeContext other = (TypeContext) arg0;

        String qf1 = getQualifiedName();
        String qf2 = other.getQualifiedName();
        
        // FIXME it is unclear why we need this. Normally the
        //  replacement patterns should have covered this.
        
        // changes the order of types that start with the same prefix
        if (qf2.startsWith(qf1)) {
            return -(qf1.compareTo(qf2));
        }

        return qf1.compareTo(qf2);
    }

    public boolean isValidType() {
        if (getType() == 19) return true;
        if (getType() == 55) return true;

        String type = getQualifiedName();
        int index = type.lastIndexOf('.');
        if (index != -1) {
            CharSequence lastElement = type.substring(index + 1);
            if (lastElement.toString().matches("^[A-Z]{1,}[a-z0-9]{1,}.*")) {
                return true;
            }
        }
        return false;
    }

    public void addComponent(String component) {
        sequence.add(component);
        qualifiedName = null;
    }

    public int getLength() {
        return sequence.size();
    }

}
