package sanandreasp.mods.TurretMod3.client.gui.TCU;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import sanandreasp.mods.TurretMod3.client.gui.GuiTurretButton;
import sanandreasp.mods.TurretMod3.packet.PacketHandlerCommon;
import sanandreasp.mods.TurretMod3.registry.TM3ModRegistry;
import sanandreasp.mods.TurretMod3.registry.TurretUpgrades.TUpgControl;
import sanandreasp.mods.TurretMod3.registry.TurretUpgrades.TurretUpgrades;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.packet.Packet250CustomPayload;

public class GuiTCUSettings extends GuiTCUBase {
	private GuiButton dismantleTurret;
	private GuiButton toggleUniqueTarget;
	private GuiButton getExperience;
	private GuiButton dismountFromBase;
	private GuiButton rideTurret;
	private GuiButton switchOnOff;
	private GuiTextField frequency;
	
	@Override
	public void initGui() {
		this.xSize = 176;
		this.ySize = 222;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
        
		this.dismantleTurret = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 30, langman.getTranslated("turretmod3.gui.tcu.stgDismantle"));
		this.buttonList.add(this.dismantleTurret);
		this.toggleUniqueTarget = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 46, langman.getTranslated("turretmod3.gui.tcu.stgUniqueTarget").split("\\|")[0]);
		this.buttonList.add(this.toggleUniqueTarget);
		this.getExperience = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 62, langman.getTranslated("turretmod3.gui.tcu.stgGetExp"));
		this.buttonList.add(this.getExperience);
		this.dismountFromBase = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 78, langman.getTranslated("turretmod3.gui.tcu.stgDismountBase"));
		this.buttonList.add(this.dismountFromBase);
		this.rideTurret = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 94, langman.getTranslated("turretmod3.gui.tcu.rideTurret"));
		this.buttonList.add(this.rideTurret);
		this.frequency = new GuiTextField(this.fontRenderer, this.guiLeft + 9, this.guiTop + 119, 158, 12);
		this.frequency.setText(Integer.toString(this.turret.getFrequency()));
		this.switchOnOff = new GuiTurretButton(this.buttonList.size(), this.guiLeft + 9, this.guiTop + 136, langman.getTranslated("turretmod3.gui.tcu.turretOnOff").split("\\|")[0]);
		this.buttonList.add(this.switchOnOff);
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();

		this.mc.func_110434_K().func_110577_a(TM3ModRegistry.TEX_GUITCUDIR + "page_2.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
        
        String s = this.turret != null ? this.turret.tInfo.getTurretName() : "";
        this.fontRenderer.drawString("\247a"+s, this.guiLeft + (this.xSize - this.fontRenderer.getStringWidth(s))/2, this.guiTop + 207, 0xFFFFFF);
        
        s = langman.getTranslated("turretmod3.gui.tcu.titSettings");
        this.fontRenderer.drawString(s, this.guiLeft + 6, this.guiTop + 6, 0x808080);
        
        if (this.turret != null) {
        	this.dismantleTurret.enabled = true;
        	this.toggleUniqueTarget.enabled = true;
        	this.switchOnOff.enabled = true;
        	this.getExperience.enabled = this.turret.getExperience() > 0;
        	this.dismountFromBase.enabled = this.turret.isRiding();
        	this.rideTurret.enabled = TurretUpgrades.hasUpgrade(TUpgControl.class, this.turret.upgrades) && this.turret.ridingEntity == null;
        	
        	String s1[] = langman.getTranslated("turretmod3.gui.tcu.stgUniqueTarget").split("\\|");
        	this.toggleUniqueTarget.displayString = s1[0] + ": " + (this.turret.useUniqueTargets() ? s1[1] : s1[2]);
        	
        	s1 = langman.getTranslated("turretmod3.gui.tcu.turretOnOff").split("\\|");
        	this.switchOnOff.displayString = (this.turret.isActive() ? s1[1] : s1[0]);
        } else {
        	this.dismantleTurret.enabled = false;
        	this.toggleUniqueTarget.enabled = false;
        	this.getExperience.enabled = false;
        	this.dismountFromBase.enabled = false;
        	this.rideTurret.enabled = false;
        	this.switchOnOff.enabled = false;
        }
        
		super.drawScreen(par1, par2, par3);
		
		s = langman.getTranslated("turretmod3.gui.tcu.frequency");
		this.fontRenderer.drawString(s, this.guiLeft + 9, this.guiTop + 110, 0x606060);
		
		this.frequency.drawTextBox();
	}
	
    @Override
	public void updateScreen()
    {
    	super.updateScreen();
    	this.frequency.updateCursorCounter();
    }
    
    @Override
	protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        this.frequency.mouseClicked(par1, par2, par3);
    }
	
    @Override
	protected void keyTyped(char par1, int par2)
    {
    	this.frequency.textboxKeyTyped(par1, par2);

    	if ((par2 == 28 || par2 == 1) && this.frequency.isFocused()) {
    		this.frequency.setFocused(false);
    		if (!this.frequency.getText().isEmpty()) {
    			this.writeFrequency();
    		}
    	}
    	else if ((par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode) && !this.frequency.isFocused()) {
    		this.mc.thePlayer.closeScreen();
    		this.writeFrequency();
    	}
    }
    
    private void writeFrequency() {
    	try {
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		DataOutputStream dos = new DataOutputStream(bos);
    		
    		dos.writeInt(0x002);
			dos.writeInt(this.turret.entityId);
			dos.writeByte(0x5);
    		dos.writeUTF(this.frequency.getText());
    		
    		PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(PacketHandlerCommon.getChannel(), bos.toByteArray()));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		byte b = 0x7;
		if (par1GuiButton.id == this.dismantleTurret.id) b = 0x0;
		else if (par1GuiButton.id == this.toggleUniqueTarget.id) b = 0x1;
		else if (par1GuiButton.id == this.getExperience.id) b = 0x2;
		else if (par1GuiButton.id == this.dismountFromBase.id) b = 0x3;
		else if (par1GuiButton.id == this.rideTurret.id) b = 0x4;
		else if (par1GuiButton.id == this.switchOnOff.id) b = 0x6;
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(bos);
			
			dos.writeInt(0x002);
			dos.writeInt(this.turret.entityId);
			dos.writeByte(b);
			
			PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(PacketHandlerCommon.getChannel(), bos.toByteArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (b == 0x0 || b == 0x4 || (b == 0x6 && this.turret.isActive())) {
			this.mc.thePlayer.closeScreen();
			return;
		}
		
		super.actionPerformed(par1GuiButton);
	}
}
