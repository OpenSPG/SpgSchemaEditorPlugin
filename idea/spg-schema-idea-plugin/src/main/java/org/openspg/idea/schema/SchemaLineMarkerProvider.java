package org.openspg.idea.schema;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openspg.idea.schema.grammar.psi.SchemaTypes;
import org.openspg.idea.schema.lang.psi.SchemaEntityHead;
import org.openspg.idea.schema.lang.psi.SchemaVariableStructureType;
import org.openspg.idea.schema.util.PsiUtils;

import java.util.Collection;

public final class SchemaLineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        if (element instanceof SchemaEntityHead entityHead) {
            handleEntityHead(entityHead, result);
        }
    }

    private void handleEntityHead(@NotNull SchemaEntityHead entityHead,
                                  @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        String entityName = entityHead.getBasicStructureDeclaration().getName();

        PsiElement[] targetElements = PsiUtils.searchChildrenOfAnyType(entityHead.getContainingFile(), true, SchemaTypes.VARIABLE_STRUCTURE_TYPE)
                .stream()
                .map(x -> (SchemaVariableStructureType) x)
                .filter(variableStructureType -> StringUtils.equals(entityName, variableStructureType.getText()))
                .toArray(PsiElement[]::new);

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder
                .create(SchemaIcons.FILE)
                .setTargets(targetElements)
                .setTooltipText(SchemaBundle.message("SchemaLineMarkerProvider.navigate.to.usages"));

        result.add(builder.createLineMarkerInfo(entityHead));
    }

}
