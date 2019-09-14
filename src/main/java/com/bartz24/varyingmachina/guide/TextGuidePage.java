package com.bartz24.varyingmachina.guide;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextGuidePage implements IGuidePage {

    private ResourceLocation id;
    private ItemStack stackIcon;

    protected List<GuiPageButton> buttons = new ArrayList<>();
    private List<List<Object>> display = new ArrayList<>();

    private List<Object> currentLine = new ArrayList<>();

    public TextGuidePage(ResourceLocation id, ItemStack stackIcon) {
        this.id = id;
        this.stackIcon = stackIcon;
        GuideRegistry.addPage(id, this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(id.toString().replace(":", ".") + ".page.name");
    }

    @Override
    public ItemStack getItemStackIcon() {
        return stackIcon;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void load(GuiPageInfo gui) {
        display = setupPage(gui, gui.getWidth());
    }

    @Override
    public void drawSlot(GuiPageInfo gui, int x, int y, double mouseX, double mouseY) {
        int curX = x;
        int curY = y;
        for (List<Object> list : display) {
            for (Object obj : list) {
                if (obj instanceof String) {
                    if (obj.toString().equals("*gl")) {
                        continue;
                    } else if (obj.toString().equals("*nl")) {
                        continue;
                    }
                    Minecraft.getInstance().fontRenderer.drawString(obj.toString(), curX, curY, 16777215);
                    curX += Minecraft.getInstance().fontRenderer.getStringWidth(obj.toString());
                } else if (obj instanceof GuiPageButton) {
                    GuiPageButton button = (GuiPageButton) obj;
                    button.x = curX;
                    button.y = curY - 4;
                    button.render((int) mouseX, (int) mouseY, 0);
                    curX += button.getWidth();
                }
            }
            curX = x;
            curY += 16;
        }
    }

    @Override
    public int getHeight(int width) {
        return setupPage(null, width).size() * 16;
    }

    List<List<Object>> setupPage(GuiPageInfo gui, int width) {
        String info = new TranslationTextComponent(id.toString().replace(":", ".") + ".page.text").getFormattedText();

        info = info.replaceAll("\\t", "    ");

        HashMap<Integer, String> specialArgs = new HashMap<>();

        Pattern pattern = Pattern.compile("<(.+)>");
        Matcher matcher = pattern.matcher(info);
        if (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String group = matcher.group(i);
                specialArgs.put(matcher.start(i), group);
                info = info.substring(0, matcher.start(i)) + group.replaceAll(".", "*") + info.substring(matcher.end(i));
            }
        }

        String[] words = info.split(" ");

        if (gui != null)
            buttons.clear();
        List<List<Object>> richTextLines = new ArrayList<>();

        currentLine = new ArrayList<>();

        int curIndexInString = 0;

        for (String word : words) {
            processWord(gui, word, richTextLines, width, curIndexInString, specialArgs);
            curIndexInString += word.length() + 1;
        }
        richTextLines.add(currentLine);

        return richTextLines;
    }

    private void processWord(GuiPageInfo pageInfo, String word, List<List<Object>> richTextLines, int maxWidth, int curIndex, HashMap<Integer, String> specialArgs) {

        boolean hasSpecial = false;
        while (true) {
            int nextSpecial = getNextSpecial(specialArgs, curIndex + word.length());
            if (nextSpecial == Integer.MAX_VALUE) break;
            hasSpecial = true;
            String wordBefore = word.substring(0, nextSpecial - curIndex);
            processWord(pageInfo, wordBefore, richTextLines, maxWidth, curIndex, specialArgs);

            String special = specialArgs.get(nextSpecial);
            String[] args = special.substring(1, special.length() - 1).split(",");
            if (args.length > 0) {
                Object newObject = null;
                if (args[0].trim().equals("jeiButton")) {
                    newObject = new JEICategoryButton(pageInfo == null ? null : pageInfo.getGuiGuide(), 0, -100, -100, args);
                    if (pageInfo != null)
                        buttons.add((GuiPageButton) newObject);
                }

                if (getLineWidth(currentLine) + getWordWidth(newObject) > maxWidth) {
                    richTextLines.add(currentLine);
                    currentLine = new ArrayList<>();
                }
                currentLine.add(newObject);
            }
            specialArgs.remove(nextSpecial);
            String wordAfter = word.substring(nextSpecial - curIndex + special.length());
            processWord(pageInfo, wordAfter, richTextLines, maxWidth, curIndex, specialArgs);
        }
        if (hasSpecial)
            return;

        if (word.contains("\n")) {
            String[] words = word.split("\\n", -1);
            int length = 0;
            for (int i = 0; i < words.length; i++) {
                processWord(pageInfo, words[i], richTextLines, maxWidth, curIndex + length, specialArgs);
                length += words[i].length() + 1;
                if (i < words.length - 1) {
                    richTextLines.add(currentLine);
                    currentLine = new ArrayList<>();
                }
            }
        } else {
            if (getLineWidth(currentLine) + getWordWidth(word) > maxWidth) {
                richTextLines.add(currentLine);
                currentLine = new ArrayList<>();
            }
            currentLine.add(word + " ");
        }
    }

    private int getNextSpecial(HashMap<Integer, String> specialArgs, int endOfWord) {
        int lowest = Integer.MAX_VALUE;
        for (int i : specialArgs.keySet()) {
            if (i < endOfWord && i < lowest)
                lowest = i;
        }
        return lowest;
    }

    private int getLineWidth(List<Object> line) {
        int width = 0;
        for (Object obj : line) {
            width += getWordWidth(obj);
        }
        return width;
    }

    private int getWordWidth(Object obj) {
        if (obj == null)
            return 0;
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        if (obj instanceof Button)
            return ((Button) obj).getWidth() + 4;
        return fontRenderer.getStringWidth(obj.toString() + " ");
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return this;
    }

    public GuiPageButton getHoveredButton(double mouseX, double mouseY) {
        for (GuiPageButton button : buttons) {
            if (mouseX >= button.x && mouseX < button.x + button.getWidth() && mouseY >= button.y && mouseY < button.y + button.getHeight())
                return button;
        }
        return null;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        if (getHoveredButton(mouseX, mouseY) != null)
            getHoveredButton(mouseX, mouseY).mouseClicked(mouseX, mouseY, clickType);
        return false;
    }

    @Override
    public List<String> getTooltip(double mouseX, double mouseY) {
        List<String> list = new ArrayList<>();
        if (getHoveredButton(mouseX, mouseY) != null)
            list.addAll(getHoveredButton(mouseX, mouseY).getTooltip(mouseX, mouseY));
        return list;
    }
}
