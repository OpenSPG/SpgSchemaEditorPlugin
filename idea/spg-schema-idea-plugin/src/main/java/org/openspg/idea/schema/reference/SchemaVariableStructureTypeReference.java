package org.openspg.idea.schema.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.SchemaIcons;
import org.openspg.idea.schema.grammar.psi.SchemaTypes;
import org.openspg.idea.schema.lang.psi.*;
import org.openspg.idea.schema.util.PsiUtils;

public class SchemaVariableStructureTypeReference extends PsiPolyVariantReferenceBase<SchemaVariableStructureType> {

    private final String myEntityName;

    public SchemaVariableStructureTypeReference(@NotNull SchemaVariableStructureType element) {
        super(element, new TextRange(0, element.getTextLength()));
        this.myEntityName = element.getText();
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return PsiUtils.searchChildrenOfAnyType(myElement.getContainingFile(), true, SchemaTypes.ENTITY_HEAD)
                .stream()
                .map(x -> ((SchemaEntityHead) x))
                .map(SchemaEntityHead::getBasicStructureDeclaration)
                .map(SchemaBasicStructureDeclaration::getStructureNameDeclaration)
                .map(SchemaStructureNameDeclaration::getStructureName)
                .filter(x -> StringUtils.equals(x.getText(), this.myEntityName))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return PsiTreeUtil.getChildrenOfTypeAsList(myElement.getContainingFile(), SchemaEntity.class)
                .stream()
                .map(x -> LookupElementBuilder
                        .create(x)
                        .withIcon(SchemaIcons.FILE)
                        .withTypeText(x.getContainingFile().getName())
                )
                .toArray(LookupElement[]::new);
    }

}
