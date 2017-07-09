package com.pmqin.kafka;

public class getMessage {

	public static void main(String[] args) {

		LogConsumer consumer = null;
		try {
			MessageExecutor executor = new MessageExecutor() {

				public void execute(String message) {
					System.out.println("--显示而已--" + message);
				}
			};
			consumer = new LogConsumer("pmqin", 5, executor);
			consumer.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

}
