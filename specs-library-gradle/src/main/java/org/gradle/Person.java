package org.gradle;

import org.apache.commons.collections.list.GrowthList;

import com.google.common.base.Preconditions;


public class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
        new GrowthList();
    }

    public String getName() {
    	Preconditions.checkArgument(true);
        return name;
    }
}
