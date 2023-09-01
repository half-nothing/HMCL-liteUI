/*
 * Hello Minecraft! Launcher
 * Copyright (C) 2020  huangyuhui <huanghongxun2008@126.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.jackhuang.hmcl.ui.construct;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.jackhuang.hmcl.ui.FXUtils;
import org.jackhuang.hmcl.util.Pair;

import static org.jackhuang.hmcl.util.Pair.pair;

public class AdvancedListItem extends Control {
    private final ObjectProperty<Node> leftGraphic = new SimpleObjectProperty<>(this, "leftGraphic");
    private final ObjectProperty<Node> rightGraphic = new SimpleObjectProperty<>(this, "rightGraphic");
    private final StringProperty title = new SimpleStringProperty(this, "title");
    private final BooleanProperty active = new SimpleBooleanProperty(this, "active");
    private final StringProperty subtitle = new SimpleStringProperty(this, "subtitle");
    private final BooleanProperty actionButtonVisible = new SimpleBooleanProperty(this, "actionButtonVisible", true);
    private ObjectProperty<EventHandler<ActionEvent>> onAction = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onAction") {
        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }
    };

    public AdvancedListItem() {
        getStyleClass().add("advanced-list-item");
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> fireEvent(new ActionEvent()));
    }

    public static Pair<Node, ImageView> createImageView(Image image) {
        return createImageView(image, 32, 32);
    }

    public static Pair<Node, ImageView> createImageView(Image image, double width, double height) {
        StackPane imageViewContainer = new StackPane();
        FXUtils.setLimitWidth(imageViewContainer, width);
        FXUtils.setLimitHeight(imageViewContainer, height);

        ImageView imageView = new ImageView();
        FXUtils.limitSize(imageView, width, height);
        imageView.setPreserveRatio(true);
        imageView.setImage(image);
        imageViewContainer.getChildren().setAll(imageView);
        return pair(imageViewContainer, imageView);
    }

    public Node getLeftGraphic() {
        return leftGraphic.get();
    }

    public void setLeftGraphic(Node leftGraphic) {
        this.leftGraphic.set(leftGraphic);
    }

    public ObjectProperty<Node> leftGraphicProperty() {
        return leftGraphic;
    }

    public Node getRightGraphic() {
        return rightGraphic.get();
    }

    public void setRightGraphic(Node rightGraphic) {
        this.rightGraphic.set(rightGraphic);
    }

    public ObjectProperty<Node> rightGraphicProperty() {
        return rightGraphic;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public String getSubtitle() {
        return subtitle.get();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle.set(subtitle);
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public boolean isActionButtonVisible() {
        return actionButtonVisible.get();
    }

    public void setActionButtonVisible(boolean actionButtonVisible) {
        this.actionButtonVisible.set(actionButtonVisible);
    }

    public BooleanProperty actionButtonVisibleProperty() {
        return actionButtonVisible;
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().get();
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new AdvancedListItemSkin(this);
    }
}
