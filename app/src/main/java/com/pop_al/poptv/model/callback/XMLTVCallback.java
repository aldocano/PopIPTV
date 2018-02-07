package com.pop_al.poptv.model.callback;

import com.pop_al.poptv.model.pojo.XMLTVProgrammePojo;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by DIVITECH ICT on 18-02-07.
 */

@Root(name = "tv", strict = false)
public class XMLTVCallback {
    @ElementList(inline = true, required = false)
    public List<XMLTVProgrammePojo> programmePojos;

    public String toString() {
        return "ClassPojo [programmePojos= " + this.programmePojos + "]";
    }
}