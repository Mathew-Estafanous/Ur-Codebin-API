package com.urcodebin.api.controllers.requestbody;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.urcodebin.api.enums.PasteExpiration;
import com.urcodebin.api.enums.PasteSyntax;
import com.urcodebin.api.enums.PasteVisibility;

/**
 * Object that is used when handling with CodePaste Request body.
 * Largely used with writing the JSON body to an object that can
 * then be passed to other services to be used. This is a data
 * transfer object and not meant to be mutated.
 */
public class PasteRequestBody {

    @JsonProperty(value = "paste_title")
    @JsonSetter(nulls = Nulls.SKIP)
    private final String pasteTitle;

    @JsonProperty(value = "paste_syntax")
    @JsonSetter(nulls = Nulls.SKIP)
    private final PasteSyntax pasteSyntax;

    @JsonProperty(value = "paste_visibility")
    @JsonSetter(nulls = Nulls.SKIP)
    private final PasteVisibility pasteVisibility;

    @JsonProperty(value = "paste_expiration")
    @JsonSetter(nulls = Nulls.SKIP)
    private final PasteExpiration pasteExpiration;

    @JsonProperty(value = "source_code")
    private final String sourceCode;

    /*
     * Constructor starts out using the default values for each request body
     * and then Jackson will set non-null values to each, if possible.
     */
    public PasteRequestBody() {
        this.pasteTitle = "Untitled Paste";
        this.pasteSyntax = PasteSyntax.NONE;
        this.pasteVisibility = PasteVisibility.PRIVATE;
        this.pasteExpiration = PasteExpiration.ONEHOUR;
        this.sourceCode = "";
    }

    public String getPasteTitle() {
        return pasteTitle;
    }

    public PasteSyntax getPasteSyntax() {
        return pasteSyntax;
    }

    public PasteVisibility getPasteVisibility() {
        return pasteVisibility;
    }

    public PasteExpiration getPasteExpiration() {
        return pasteExpiration;
    }

    public String getSourceCode() {
        return sourceCode;
    }
}
