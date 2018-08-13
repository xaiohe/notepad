package com.jframe;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;

public class JFrameExample extends JFrame {
	
	//全局的变量
	File currentFile;
	static JTextArea textArea;
	private JPanel contentPane;
	UndoManager undo = new UndoManager();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameExample frame = new JFrameExample();
					frame.setVisible(true);
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							//未保存过，就不能执行
							if(frame.currentFile!=null){
								if(frame.lastModifyContent == null||frame.lastModifyContent.equals(textArea.getText())){
									return;
								}
								frame.autoSaveFile();
							}
						}
					}, 1000,3000);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Timer to = new Timer();
		to.schedule(new TimerTask() {
			@Override
			public void run() {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("D:\\a.txt"));
				chooser.setDialogTitle("小何记事本的文件选择器");
				File file = chooser.getSelectedFile();
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(textArea.getText());
					fw.flush();
					fw.close();
				} catch (Exception e2) {
				}
			}

		}, 0, 1000);
	}

	/**
	 * Create the frame.
	 */
	public JFrameExample() {
		textArea = new JTextArea();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("小何的记事本");
		setIconImage(Toolkit.getDefaultToolkit().getImage("3.png"));
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("文件(F)");
		menuBar.add(menu);
		Icon o = new ImageIcon("1.png");
		JMenuItem menuItem = new JMenuItem("新建(N)", o);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		menu.add(menuItem);

		JMenuItem mntmo = new JMenuItem("打开(O)");
		mntmo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();
				jf.setCurrentDirectory(new File("G:\\"));
				jf.showOpenDialog(mntmo);
				File file = jf.getSelectedFile();
				Scanner s = null;
				try {
					s = new Scanner(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				StringBuffer sb = new StringBuffer();
				while (s.hasNextLine()) {
					sb.append(s.nextLine()).append("\r\n");
				}
				textArea.setText(sb.toString());
			}
		});
		menu.add(mntmo);

		JMenuItem mntms = new JMenuItem("保存(S)");
		JFrameExample that = this;
		mntms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("G:\\"));
				chooser.setDialogTitle("小何记事本的文件选择器");
				chooser.showSaveDialog(mntms);
				File file = chooser.getSelectedFile();
				currentFile = file;
				that.setTitle(file.getName()+"----"+file.getAbsolutePath());
				try {
					lastModifyContent=textArea.getText();
					FileWriter fw = new FileWriter(file);
					fw.write(textArea.getText());
					fw.flush();
					fw.close();
				} catch (Exception e2) {
				}
			}
		});
		menu.add(mntms);

		JMenuItem mntma = new JMenuItem("另存为(A)");
		mntma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(""));
				chooser.setDialogTitle("小何记事本的文件选择器");
				chooser.showSaveDialog(mntms);
				File file = chooser.getSelectedFile();
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(textArea.getText());
					fw.flush();
					fw.close();
				} catch (Exception e2) {
				}
			}
		});
		menu.add(mntma);

		JMenuItem mntmu = new JMenuItem("页面设置(U)");
		menu.add(mntmu);

		JMenuItem mntmp = new JMenuItem("打印(P)");
		menu.add(mntmp);

		JMenuItem mntmx = new JMenuItem("退出(X)");
		mntmx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(mntmx);

		JMenu menu_1 = new JMenu("编辑(E)");
		menuBar.add(menu_1);

		JMenuItem mntmu_1 = new JMenuItem("撤消(U)");
		mntmu_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.getDocument().addUndoableEditListener(undo);

			}
		});
		menu_1.add(mntmu_1);

		JMenuItem mntmt = new JMenuItem("剪切(T)");
		mntmt.addActionListener(e -> {
			String text = textArea.getSelectedText();
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(new StringSelection(text), null);
			textArea.replaceRange(null, textArea.getSelectionStart(),
					textArea.getSelectionEnd());

		});

		menu_1.add(mntmt);

		JMenuItem mntmc = new JMenuItem("复制(C)");
		mntmc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getSelectedText();
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(new StringSelection(text), null);
			}
		});
		menu_1.add(mntmc);

		JMenuItem mntmp_1 = new JMenuItem("粘贴(P)");
		mntmp_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String content = (String) Toolkit.getDefaultToolkit()
							.getSystemClipboard()
							.getData(DataFlavor.stringFlavor);
					textArea.insert(content, textArea.getCaretPosition());
				} catch (Exception e2) {
				}
			}
		});
		menu_1.add(mntmp_1);

		JMenuItem mntml = new JMenuItem("删除(L)");
		mntml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getSelectedText();
				textArea.replaceRange(null, textArea.getSelectionStart(),
						textArea.getSelectionEnd());
			}
		});
		menu_1.add(mntml);

		JMenuItem mntmf = new JMenuItem("查找(F)");
		menu_1.add(mntmf);

		JMenuItem mntmn = new JMenuItem("查找下一个(N)");
		menu_1.add(mntmn);

		JMenuItem mntmr = new JMenuItem("替换(R)");
		menu_1.add(mntmr);

		JMenuItem mntmg = new JMenuItem("转到(G)");
		menu_1.add(mntmg);

		JMenuItem mntma_1 = new JMenuItem("全选(A)");
		menu_1.add(mntma_1);

		JMenuItem mntmd = new JMenuItem("时间/日期(D)");
		mntmd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tt();
			}
		});
		menu_1.add(mntmd);

		JMenu menu_2 = new JMenu("格式(O)");
		menuBar.add(menu_2);

		JMenuItem mntmw = new JMenuItem("自动换行(W)");
		menu_2.add(mntmw);

		JMenuItem mntmf_1 = new JMenuItem("字体(F)");
		menu_2.add(mntmf_1);

		JMenu menu_3 = new JMenu("查看(V)");
		menuBar.add(menu_3);

		JMenuItem mntms_1 = new JMenuItem("状态栏(S)");
		menu_3.add(mntms_1);
		JMenuItem mntms_2 = new JMenuItem("复制路径(S)");
		mntms_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(currentFile.getAbsolutePath());
				
			}
		});
		menu_3.add(mntms_2);

		JMenu menu_4 = new JMenu("帮助(H)");
		menuBar.add(menu_4);

		JMenuItem mntmh = new JMenuItem("查看帮助(H)");
		menu_4.add(mntmh);

		JMenuItem mntma_2 = new JMenuItem("关于记事本(A)");
		menu_4.add(mntma_2);
		JPopupMenu pop = new JPopupMenu();
		JMenuItem copy = new JMenuItem("复制");
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getSelectedText();
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(new StringSelection(text), null);
			}
		});
		pop.add(copy);

		JMenuItem cut = new JMenuItem("剪切");
		cut.addActionListener(e -> {
			String text = textArea.getSelectedText();
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(new StringSelection(text), null);
			textArea.replaceRange(null, textArea.getSelectionStart(),
					textArea.getSelectionEnd());
		});
		pop.add(cut);
		JMenuItem paste = new JMenuItem("粘贴");
		pop.add(paste);
		paste.addActionListener(e -> {
			try {
				String content = (String) Toolkit.getDefaultToolkit()
						.getSystemClipboard().getData(DataFlavor.stringFlavor);
				textArea.insert(content, textArea.getCaretPosition());

			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});
		textArea.setInheritsPopupMenu(true);
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON3) {
					pop.show(textArea, e.getX(), e.getY());
					pop.setVisible(true);
				}
			};
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		scrollPane.setViewportView(textArea);
	}
	String lastModifyContent;
	public void autoSaveFile(){
		
		
		
		try {
			PrintWriter pw = new PrintWriter(currentFile);
			if(lastModifyContent == null||lastModifyContent.equals(textArea.getText())){
				return;
			}
			System.out.println("自动保存");
			textArea.getText();
			pw.println();
			pw.flush();
			pw.close();
			lastModifyContent = textArea.getText();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void tt() {
		Timer ti = new Timer();
		ti.schedule(new TimerTask() {
			@Override
			public void run() {
				SimpleDateFormat sf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				String date = sf.format(new Date());
				textArea.setText(date);
				
				
				
			}
		}, 0, 1000);
	}
}
