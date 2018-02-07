package com.pop_al.poptv.model.pojo;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

@Root(name = "icon", strict = false)
public class XMLTVIconPojo {
    @Attribute(name = "src", required = false)
    private String src;

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String toString() {
        return "ClassPojo [src = " + this.src + "]";
    }
}
