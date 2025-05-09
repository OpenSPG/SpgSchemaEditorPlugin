package org.openspg.idea.conceptRule.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.conceptRule.demo.ConceptRuleDemo;
import org.openspg.idea.schema.SchemaIcons;

import javax.swing.*;
import java.util.Map;

final class ConceptRuleColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", ConceptRuleHighlightingColors.KEYWORD),
            new AttributesDescriptor("Comments//Block comment", ConceptRuleHighlightingColors.BLOCK_COMMENT),
            new AttributesDescriptor("Comments//Line comment", ConceptRuleHighlightingColors.LINE_COMMENT),
            new AttributesDescriptor("Error", ConceptRuleHighlightingColors.ERROR),

            new AttributesDescriptor("Namespace//Key", ConceptRuleHighlightingColors.NAMESPACE_KEY),
            new AttributesDescriptor("Namespace//Value", ConceptRuleHighlightingColors.NAMESPACE_VALUE),

            new AttributesDescriptor("Concept rule//Pattern", ConceptRuleHighlightingColors.WRAPPER_PATTERN),
            new AttributesDescriptor("Concept rule//Category", ConceptRuleHighlightingColors.WRAPPER_FIELD),

            new AttributesDescriptor("Number", ConceptRuleHighlightingColors.NUMBER),
            new AttributesDescriptor("String", ConceptRuleHighlightingColors.STRING),
            new AttributesDescriptor("Variables", ConceptRuleHighlightingColors.VARIABLES),

            new AttributesDescriptor("Braces and Operators//Braces", ConceptRuleHighlightingColors.BRACES),
            new AttributesDescriptor("Braces and Operators//Brackets", ConceptRuleHighlightingColors.BRACKETS),
            new AttributesDescriptor("Braces and Operators//Comma", ConceptRuleHighlightingColors.COMMA),
            new AttributesDescriptor("Braces and Operators//Dot", ConceptRuleHighlightingColors.DOT),
            new AttributesDescriptor("Braces and Operators//Operator sign", ConceptRuleHighlightingColors.OPERATION_SIGN),
            new AttributesDescriptor("Braces and Operators//Parentheses", ConceptRuleHighlightingColors.PARENTHESES),
            new AttributesDescriptor("Braces and Operators//Semicolon", ConceptRuleHighlightingColors.SEMICOLON),
    };

    @Override
    public Icon getIcon() {
        return SchemaIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new ConceptRuleSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return ConceptRuleDemo.getHighlighterText();
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "OpenSPG Concept Rule";
    }

}
