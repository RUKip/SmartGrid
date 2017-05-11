package com.rug.energygrid.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Ruben on 08-May-17.
 */
public class JSON_Array_Group<T> {
        private String name;
        private List<T> list;

        public JSON_Array_Group(String n, List l){
            this.name = n;
            this.list = l;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List getGroup() {
            return list;
        }

        public void setGroup(List l) {
            this.list = l;
        }
}
