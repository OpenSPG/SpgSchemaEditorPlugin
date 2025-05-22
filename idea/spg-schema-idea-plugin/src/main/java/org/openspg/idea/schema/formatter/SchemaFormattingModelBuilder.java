package org.openspg.idea.schema.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaLanguage;
import org.openspg.idea.schema.grammar.psi.SchemaTypes;

public final class SchemaFormattingModelBuilder implements FormattingModelBuilder {

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new SchemaBlock(
                        formattingContext.getNode(),
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        createSpaceBuilder(codeStyleSettings)
                ),
                codeStyleSettings
        );
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        CommonCodeStyleSettings commonSetting = settings.getCommonSettings(SchemaLanguage.INSTANCE.getID());

        SpacingBuilder builder = new SpacingBuilder(settings, SchemaLanguage.INSTANCE);
        builder = builder.after(SchemaTypes.NAMESPACE_KEYWORD).spaces(1);

        int blankLinesAfterNamespace = Math.max(0, commonSetting.BLANK_LINES_AFTER_PACKAGE) + 1;
        builder = builder.after(TokenSet.create(SchemaTypes.NAMESPACE))
                .spacing(0, 0, blankLinesAfterNamespace, false, 0);

        int blankLinesAfterEntity = Math.max(0, commonSetting.BLANK_LINES_AFTER_IMPORTS) + 1;
        builder = builder.after(TokenSet.create(SchemaTypes.ENTITY))
                .spacing(0, 0, blankLinesAfterEntity, false, 0);

        builder = initSpaceBuilderByIndent(builder, commonSetting);
        builder = initSpaceBuilderByComma(builder, commonSetting);
        builder = initSpaceBuilderByColon(builder, commonSetting);

        boolean spaceAroundBrackets = commonSetting.SPACE_WITHIN_BRACKETS;
        if (spaceAroundBrackets) {
            builder = builder
                    .before(SchemaTypes.DOUBLE_LBRACKET).spaces(1)
                    .after(SchemaTypes.DOUBLE_RBRACKET).spaces(1);
        } else {
            builder = builder
                    .around(SchemaTypes.DOUBLE_LBRACKET).spaces(0)
                    .around(SchemaTypes.DOUBLE_RBRACKET).spaces(0);
        }

        //.after(TokenSet.create(
        //        SchemaTypes.ENTITY_INFO, SchemaTypes.ENTITY_META, SchemaTypes.ENTITY_META_INFO,
        //        SchemaTypes.PROPERTY_INFO, SchemaTypes.PROPERTY_META_INFO,
        //        SchemaTypes.SUB_PROPERTY_INFO, SchemaTypes.SUB_PROPERTY_META,
        //        SchemaTypes.LINE_COMMENT
        //))
        //.spacing(0, 0, 1, false, 0)

        return builder;
    }

    private static SpacingBuilder initSpaceBuilderByIndent(SpacingBuilder builder, CommonCodeStyleSettings settings) {
        CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions();
        int indentSize = indentOptions == null ? 4 : Math.max(1, indentOptions.INDENT_SIZE);

        builder = builder.after(SchemaTypes.INDENT_META)
                .spaces(indentSize - 1)

                .after(SchemaTypes.INDENT_PROP)
                .spaces(indentSize * 2 - 1)

                .after(SchemaTypes.INDENT_PROPMETA)
                .spaces(indentSize * 3 - 1)

                .after(SchemaTypes.INDENT_SUBPROP)
                .spaces(indentSize * 4 - 1)

                .after(SchemaTypes.INDENT_SUBPROPMETA)
                .spaces(indentSize * 5 - 1);
        return builder;
    }

    private static SpacingBuilder initSpaceBuilderByComma(SpacingBuilder builder, CommonCodeStyleSettings settings) {
        boolean spaceAfterComma = settings.SPACE_AFTER_COMMA;
        boolean spaceBeforeComma = settings.SPACE_BEFORE_COMMA;

        if (spaceBeforeComma && !spaceAfterComma) {
            builder = builder.before(SchemaTypes.COMMA).spaces(1);
        } else if (spaceBeforeComma) {
            builder = builder.around(SchemaTypes.COMMA).spaces(1);
        } else if (spaceAfterComma) {
            builder = builder.after(SchemaTypes.COMMA).spaces(1);
        } else {
            builder = builder.before(SchemaTypes.COMMA).spaces(0);
        }
        return builder;
    }

    private static SpacingBuilder initSpaceBuilderByColon(SpacingBuilder builder, CommonCodeStyleSettings settings) {
        boolean spaceAfterColon = settings.SPACE_AFTER_COLON;
        boolean spaceBeforeColon = settings.SPACE_BEFORE_COLON;

        TokenSet tokenSet = TokenSet.create(SchemaTypes.COLON, SchemaTypes.RIGHT_ARROW);
        if (spaceBeforeColon && !spaceAfterColon) {
            builder = builder.before(tokenSet).spaces(1);
        } else if (spaceBeforeColon) {
            builder = builder.around(tokenSet).spaces(1);
        } else if (spaceAfterColon) {
            builder = builder.after(tokenSet).spaces(1);
        } else {
            builder = builder.before(tokenSet).spaces(0);
        }
        return builder;
    }

}
