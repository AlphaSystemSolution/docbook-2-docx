package com.alphasystem.xml;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

/**
 * @author sali
 */
public final class ProcessingInstruction {

    private final String target;
    private final String data;

    public ProcessingInstruction(String target, String data) {
        this.target = target;
        this.data = data;
    }

    public String getTarget() {
        return target;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        String d = StringUtils.isBlank(data) ? "" : format(" %s", data);
        return format("<?%s%s?>", target, d);
    }
}
