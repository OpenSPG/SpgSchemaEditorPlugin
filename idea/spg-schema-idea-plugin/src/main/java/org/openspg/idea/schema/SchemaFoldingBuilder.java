package org.openspg.idea.schema;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.schema.lang.psi.SchemaEntity;
import org.openspg.idea.schema.lang.psi.SchemaEntityMeta;

import java.util.ArrayList;
import java.util.List;

public class SchemaFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        PsiTreeUtil.findChildrenOfType(root, PsiComment.class).forEach(element -> {
            descriptors.add(new FoldingDescriptor(element, element.getTextRange()));
        });

        PsiTreeUtil.findChildrenOfAnyType(root, SchemaEntity.class, SchemaEntityMeta.class).forEach(element -> {
            descriptors.add(new FoldingDescriptor(element, element.getTextRange()));
        });

        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    @Override
    public @Nullable String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        if (element instanceof PsiComment) {
            return "#...";

        } else if (element instanceof SchemaEntity entity) {
            return entity.getEntityHead().getBasicStructureDeclaration().getText() + "\n";

        } else if (element instanceof SchemaEntityMeta entityMeta) {
            return entityMeta.getEntityMetaHead().getBasicPropertyDeclaration().getText() + "\n";
        }
        return "";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        return element instanceof PsiComment;
    }
}

