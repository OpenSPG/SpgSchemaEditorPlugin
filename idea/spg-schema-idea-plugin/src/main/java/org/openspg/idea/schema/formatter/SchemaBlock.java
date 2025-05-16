package org.openspg.idea.schema.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.openspg.idea.lang.psi.SchemaPlainText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.openspg.idea.grammar.psi.SchemaTypes.EOL;

public class SchemaBlock extends AbstractBlock {

    private static final Set<IElementType> NONBLOCK_ELEMENT_TYPES = Set.of(
            TokenType.WHITE_SPACE, EOL
    );

    private final SpacingBuilder mySpacingBuilder;

    protected SchemaBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment, SpacingBuilder spacingBuilder) {
        super(node, wrap, alignment);
        mySpacingBuilder = spacingBuilder;
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        if (myNode.getPsi() instanceof SchemaPlainText) {
            return blocks;
        }

        ASTNode child = myNode.getFirstChildNode();
        while (child != null) {
            IElementType type = child.getElementType();
            if (!NONBLOCK_ELEMENT_TYPES.contains(type)) {
                blocks.add(new SchemaBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        mySpacingBuilder
                ));
            }
            child = child.getTreeNext();
        }

        return blocks;
    }

    @Override
    public Indent getIndent() {
        return Indent.getNoneIndent();
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block firstChild, @NotNull Block secondChild) {
        return mySpacingBuilder.getSpacing(this, firstChild, secondChild);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getPsi() instanceof SchemaPlainText || myNode.getFirstChildNode() == null;
    }

}
