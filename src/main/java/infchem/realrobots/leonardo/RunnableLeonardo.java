/**
 * 
 */
package infchem.realrobots.leonardo;

import infchem.realrobots.RealRobots;
import infchem.realrobots.wedo.ConnectorWeDo;
import infchem.realrobots.wedo.WeDoRedstoneMessage;
import net.minecraft.item.ItemStack;

/**
 * @author infchem
 * 
 */
public class RunnableLeonardo implements Runnable {

	private ItemStack[] commands;
	private ConnectorLeonardo cal = new ConnectorLeonardo();
	private String playername;
	private int x;
	private int y;
	private int z;

	public enum CommandsLeonardo {
		leonardoinputport0, leonardoinputport1, leonardoinputport2, leonardoinputport3, 
		leonardoinputport4, leonardoinputport5, leonardoinputport6, leonardoinputport7, 
		leonardooutputportlow0, leonardooutputportlow1, leonardooutputportlow2, 
		leonardooutputportlow3, leonardooutputportlow4, leonardooutputportlow5, 
		leonardooutputportlow6, leonardooutputportlow7, leonardooutputporthigh0, 
		leonardooutputporthigh1, leonardooutputporthigh2, leonardooutputporthigh3, 
		leonardooutputporthigh4, leonardooutputporthigh5, leonardooutputporthigh6, 
		leonardooutputporthigh7, sleep,sendredstone,
		dontsendredstone
	}

	public RunnableLeonardo(ItemStack[] commands, String playername,int x,int y,int z) {
		this.commands = commands;
		this.playername = playername;
		this.x=x;
		this.y=y;
		this.z=z;
	}

	@Override
	public void run() {

		if (commands != null) {
			if (commands[9] != null) {
				ItemStack sensorStack = commands[9];
				CommandsLeonardo sensorCommand = CommandsLeonardo
						.valueOf(sensorStack.getUnlocalizedName().substring(
								sensorStack.getUnlocalizedName().lastIndexOf(
										".") + 1));

				int inputPort = -1;

				switch (sensorCommand) {
				case leonardoinputport0:
					inputPort = 0;
					break;
				case leonardoinputport1:
					inputPort = 1;
					break;
				case leonardoinputport2:
					inputPort = 2;
					break;
				case leonardoinputport3:
					inputPort = 3;
					break;
				case leonardoinputport4:
					inputPort = 4;
					break;
				case leonardoinputport5:
					inputPort = 5;
					break;
				case leonardoinputport6:
					inputPort = 6;
					break;
				case leonardoinputport7:
					inputPort = 7;
					break;
				case sendredstone:
					break;
				case dontsendredstone:
					break;
				default:
					break;
				}

				if (inputPort != -1) {
					try {
						cal.setInputPort(inputPort);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					Object[] input = {};
					do {
						try {
							Thread.sleep(500);
							input = cal.getInput((double) (inputPort));
						} catch (Exception e) {

							e.printStackTrace();
						}
					} while (input[0].toString().equals("1"));

				}
			}

			int outputPort = -1;
			boolean outputHigh = false;
			for (int i = 0; i < commands.length - 1; i++) {
				ItemStack stack = commands[i];
				if (stack != null) {
					CommandsLeonardo currentCommand = CommandsLeonardo
							.valueOf(stack.getUnlocalizedName()
									.substring(
											stack.getUnlocalizedName()
													.lastIndexOf(".") + 1));
					switch (currentCommand) {
					case leonardooutputportlow0:
						outputPort = 0;
						outputHigh = false;
						break;
					case leonardooutputportlow1:
						outputPort = 1;
						outputHigh = false;
						break;
					case leonardooutputportlow2:
						outputPort = 2;
						outputHigh = false;
						break;
					case leonardooutputportlow3:
						outputPort = 3;
						outputHigh = false;
						break;
					case leonardooutputportlow4:
						outputPort = 4;
						outputHigh = false;
						break;
					case leonardooutputportlow5:
						outputPort = 5;
						outputHigh = false;
						break;
					case leonardooutputportlow6:
						outputPort = 6;
						outputHigh = false;
						break;
					case leonardooutputportlow7:
						outputPort = 7;
						outputHigh = false;
						break;
					case leonardooutputporthigh0:
						outputPort = 0;
						outputHigh = true;
						break;
					case leonardooutputporthigh1:
						outputPort = 1;
						outputHigh = true;
						break;
					case leonardooutputporthigh2:
						outputPort = 2;
						outputHigh = true;
						break;
					case leonardooutputporthigh3:
						outputPort = 3;
						outputHigh = true;
						break;
					case leonardooutputporthigh4:
						outputPort = 4;
						outputHigh = true;
						break;
					case leonardooutputporthigh5:
						outputPort = 5;
						outputHigh = true;
						break;
					case leonardooutputporthigh6:
						outputPort = 6;
						outputHigh = true;
						break;
					case leonardooutputporthigh7:
						outputPort = 7;
						outputHigh = true;
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
					if (outputPort != -1) {
						if (outputHigh)
							try {
								cal.setOutputHigh(outputPort);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							try {
								cal.setOutputLow(outputPort);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}

			}
		}
	}
}
