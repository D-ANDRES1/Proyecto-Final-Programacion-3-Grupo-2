package com.grupo2.app.mapper.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.grupo2.app.model.TreeResponse;
import com.grupo2.treeengine.domain.TreeView;

@Component
public class TreeViewApiMapper {

    public TreeResponse toResponse(TreeView treeView) {

        if (treeView == null) {
            return null;
        }

        TreeResponse response = new TreeResponse();

        response.setId(treeView.getId());

        response.setValue(treeView.getValue());

        List<TreeResponse> children = new ArrayList<>();

        for (TreeView child : treeView.getChildren()) {

            children.add(
                    toResponse(child)
            );
        }

        response.setChildren(children);

        return response;
    }
}