package org.openspg.idea.conceptRule.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.conceptRule.ConceptRuleLanguage;
import org.openspg.idea.conceptRule.codeStyle.ConceptRuleCodeStyleSettings;
import org.openspg.idea.grammar.psi.ConceptRuleTypes;

import static org.openspg.idea.grammar.psi.ConceptRuleTypes.*;

public final class ConceptRuleFormattingModelBuilder implements FormattingModelBuilder {

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new ConceptRuleBlock(
                        formattingContext.getNode(),
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        createSpaceBuilder(codeStyleSettings)
                ),
                codeStyleSettings
        );
    }

    @NotNull
    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        SpacingBuilder builder = new SpacingBuilder(settings, ConceptRuleLanguage.INSTANCE);

        CommonCodeStyleSettings commonSetting = settings.getCommonSettings(ConceptRuleLanguage.INSTANCE.getID());

        ConceptRuleCodeStyleSettings customSettings = settings.getCustomSettings(ConceptRuleCodeStyleSettings.class);

        builder = builder.after(NAMESPACE_KEYWORD).spaces(1);

        int blankLinesAfterNamespace = Math.max(0, commonSetting.BLANK_LINES_AFTER_PACKAGE) + 1;
        builder = builder.after(TokenSet.create(NAMESPACE))
                .spacing(0, 0, blankLinesAfterNamespace, false, 0);

        int blankLinesAfterSchemaPattern = Math.max(0, commonSetting.BLANK_LINES_AFTER_IMPORTS) + 1;
        builder = builder.after(TokenSet.create(RULE_WRAPPER))
                .spacing(0, 0, blankLinesAfterSchemaPattern, false, 0);

        builder = builder
                .before(TokenSet.create(OPEN_RULE_BLOCK))
                .spacing(1, 1, 0, false, 0)
                .after(TokenSet.create(OPEN_RULE_BLOCK))
                .spacing(0, 0, 1, false, 0)

                .around(TokenSet.create(CLOSE_RULE_BLOCK))
                .spacing(0, 0, 1, false, 0)

                .before(TokenSet.create(LBRACE))
                .spacing(1, 1, 0, false, 0)
                .after(TokenSet.create(LBRACE))
                .spacing(0, 0, 1, false, 0)
                .before(TokenSet.create(RBRACE))
                .lineBreakInCode()
//                .spacing(0, 0, 1, false, 0)
        ;

        builder = initSpaceBuilderByComma(builder, commonSetting);
        builder = initSpaceBuilderByColon(builder, commonSetting);

        boolean spaceAroundBrackets = commonSetting.SPACE_WITHIN_BRACKETS;
        if (spaceAroundBrackets) {
            builder = builder
                    .after(ConceptRuleTypes.LBRACKET).spaces(1)
                    .before(ConceptRuleTypes.RBRACKET).spaces(1);
        } else {
            builder = builder
                    .after(ConceptRuleTypes.LBRACKET).spaces(0)
                    .before(ConceptRuleTypes.RBRACKET).spaces(0);
        }

        boolean spaceAroundParentheses = commonSetting.SPACE_WITHIN_PARENTHESES;
        if (spaceAroundParentheses) {
            builder = builder
                    .after(ConceptRuleTypes.LPARENTH).spaces(1)
                    .before(ConceptRuleTypes.RPARENTH).spaces(1);
        } else {
            builder = builder
                    .after(ConceptRuleTypes.LPARENTH).spaces(0)
                    .before(ConceptRuleTypes.RPARENTH).spaces(0);
        }

        builder = builder.around(ConceptRuleTypes.IDENTIFIER).spaces(0);

        return builder;
    }

    private static SpacingBuilder initSpaceBuilderByComma(SpacingBuilder builder, CommonCodeStyleSettings settings) {
        if (settings.SPACE_BEFORE_COMMA) {
            builder = builder.before(COMMA).spaces(1);
        }

        if (settings.SPACE_AFTER_COMMA) {
            builder = builder.after(COMMA).spaces(1);
        }

        return builder;
    }

    private static SpacingBuilder initSpaceBuilderByColon(SpacingBuilder builder, CommonCodeStyleSettings settings) {
        if (settings.SPACE_BEFORE_COLON) {
            builder = builder.before(COLON).spaces(1);
        }

        if (settings.SPACE_AFTER_COLON) {
            builder = builder.after(COLON).spaces(1);
        }
        return builder;
    }

}
