/**
 * 
 */
package infchem.realrobots.wedo;


import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import infchem.realrobots.RealRobots;
import infchem.realrobots.tileentity.TileEntityWeDo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * @author infchem
 *
 */
public class RunnableWeDo implements Runnable{

	private ItemStack[] commands;
	private String playername;
	private ConnectorWeDo cwd = new ConnectorWeDo();
	private int x;
	private int y;
	private int z;
	
	
	public enum CommandsWeDo {
		wedoleftmotorcounterclockwise,
		wedoleftmotorclockwise,
		wedorightmotorcounterclockwise,
		wedorightmotorclockwise,
		wedoleftmotorstop,
		wedorightmotorstop,
		sleep,
		wedotiltnormal,
		wedotiltleft,
		wedotiltright,
		wedotiltforward,
		wedotiltback,
		wedodistancelarge,
		wedodistancemedium,
		wedodistancesmall,
		sendredstone,
		dontsendredstone
	}

	public RunnableWeDo(ItemStack[] commands, String playername,int x,int y,int z) {
		this.commands = commands;
		this.playername = playername;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	@Override
	public void run() {
		
		if (commands != null) {
			if (commands[9] != null) 
			{
			ItemStack sensorStack = commands[9];
			CommandsWeDo sensorCommand = CommandsWeDo.valueOf(sensorStack.getUnlocalizedName().substring(sensorStack.getUnlocalizedName().lastIndexOf(".") + 1));

			int distanceTrigger = 0;
			String tiltTrigger = "";
			
			switch (sensorCommand) {
			case wedodistancesmall:
				distanceTrigger = 50;
				break;
			case wedodistancemedium:
				distanceTrigger = 110;
				break;
			case wedodistancelarge:
				distanceTrigger = 125;
				break;
			case wedotiltnormal:
				tiltTrigger = "0";
				break;
			case wedotiltleft:							
				tiltTrigger = "2";
				break;
			case wedotiltright:
				tiltTrigger = "4";
				break;
			case wedotiltforward:
				tiltTrigger = "1";
				break;
			case wedotiltback:
				tiltTrigger = "3";
				break;
			case wedoleftmotorclockwise:
				break;
			case wedoleftmotorcounterclockwise:
				break;
			case wedoleftmotorstop:
				break;
			case wedorightmotorclockwise:
				break;
			case wedorightmotorcounterclockwise:
				break;
			case wedorightmotorstop:
				break;
			case sleep:
				break;
			case sendredstone:
				break;
			case dontsendredstone:
				break;
			default:
				break;
			}
					
		if (distanceTrigger != 0) {
			Object[] distance = {};
			do {
				try {
					distance = cwd.getDistance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (Integer.parseInt((String) distance[0])>distanceTrigger);
		}
		
		
		if (tiltTrigger != "") {
			Object[] tilt = {};
			do {
				try {
					tilt = cwd.getTilt();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while ((String) tilt[0] != tiltTrigger);
		}
		}
		}
		
		for (int i = 0; i < commands.length-1; i++) {
			ItemStack stack = commands[i];

			if (stack != null) {
				Double value = 100.0;

				Object[] params = new Object[2];

				long a;
				CommandsWeDo currentCommand = CommandsWeDo.valueOf(stack.getUnlocalizedName().substring(stack.getUnlocalizedName().lastIndexOf(".") + 1));
				switch (currentCommand) {
				case wedoleftmotorcounterclockwise:
					value = -100.0;
					params[0] = value;

					try {
						cwd.setLeftMotorPower((Double) value);
						cwd.motorControl();

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case wedoleftmotorclockwise:
					value = 100.0;
					params[0] = value;
					params[1] = 0;
					try {
						cwd.setLeftMotorPower((Double) value);
						cwd.motorControl();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case wedorightmotorcounterclockwise:
					value = -100.0;
					params[0] = value;
					params[1] = 0;
					try {
						cwd.setRightMotorPower((Double) value);
						cwd.motorControl();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case wedorightmotorclockwise:
					value = 100.0;
					params[0] = value;
					params[1] = 0;
					try {
						cwd.setRightMotorPower((Double) value);
						cwd.motorControl();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case wedoleftmotorstop:
					value = 0.0;
					params[0] = value;
					params[1] = 0;
					try {
						cwd.setLeftMotorPower((Double) value);
						cwd.motorControl();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case wedorightmotorstop:
					value = 0.0;
					params[0] = value;
					params[1] = 0;
					try {
						cwd.setRightMotorPower((Double) value);
						cwd.motorControl();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case sleep:
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				case sendredstone:
		        	RealRobots.network.sendToServer(new WeDoRedstoneMessage(true,x,y,z));	
					break;
				case dontsendredstone:
					RealRobots.network.sendToServer(new WeDoRedstoneMessage(false,x,y,z));	
					break;
				default:
					break;			
				}
			}

		} 
	}

	
}
