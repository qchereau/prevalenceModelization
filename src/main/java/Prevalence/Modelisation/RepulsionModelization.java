package Prevalence.Modelisation;

import java.awt.geom.Ellipse2D;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RepulsionModelization extends JPanel {
	private static final long serialVersionUID = 1L;

	// Configuration
	// Size of the window
	private static final int SIZE = 700;
	// Number of iteration for the simulation
	private static int ITERATION = 1000;
	// Number of particles
	private static int NUMBER_OF_PARTICLES = 1000;
	// Number of antiparticles
	private static int NUMBER_OF_ANTIPARTICLES = 1000;
	// Does particle and anti-particle repels each other
	private static boolean MATTER_ANTIMATTER_REPULSION = true;
	// Determine the size of the displayed circles
	private static double SIZE_DISPLAY = 4.0;
	// Step duration (The smaller this step the faster the motion)
	private static long STEP_DURATION = 500L;

	// Variable use internally please don't modify
	private static JFrame f = new JFrame();
	private static ParticleDescription[] descriptionParticle;
	private static ParticleDescription[] descriptionAntiparticle;

	public RepulsionModelization() {
		super(true);
		this.setPreferredSize(new Dimension(SIZE, SIZE));
	}

	/**
	 * Render the graphic according to the status
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(-SIZE, SIZE, -SIZE, SIZE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.black);
		for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
			ParticleDescription particleDescription = descriptionParticle[i];
			if (particleDescription.exists()) {
				final Ellipse2D.Double circle = new Ellipse2D.Double(particleDescription.getX(),
						particleDescription.getY(), SIZE_DISPLAY * Math.sqrt(particleDescription.getMass()),
						SIZE_DISPLAY * Math.sqrt(particleDescription.getMass()));
				g2d.fill(circle);
			}
		}
		g2d.setColor(Color.red);
		for (int i = 0; i < NUMBER_OF_ANTIPARTICLES; ++i) {
			ParticleDescription antiparticleDescription = descriptionAntiparticle[i];
			if (antiparticleDescription.exists()) {
				final Ellipse2D.Double circle = new Ellipse2D.Double(antiparticleDescription.getX(),
						antiparticleDescription.getY(), SIZE_DISPLAY * Math.sqrt(antiparticleDescription.getMass()),
						SIZE_DISPLAY * Math.sqrt(antiparticleDescription.getMass()));
				g2d.fill(circle);
			}
		}
	}

	private static void create() {
		descriptionParticle = generateDescription(NUMBER_OF_PARTICLES);
		descriptionAntiparticle = generateDescription(NUMBER_OF_ANTIPARTICLES);
		f.setDefaultCloseOperation(3);
		f.add(new RepulsionModelization());
		f.pack();
		f.setVisible(true);
	}

	private static ParticleDescription[] generateDescription(int numberOfParticle) {
		ParticleDescription[] result = new ParticleDescription[numberOfParticle];
		for (int i = 0; i < numberOfParticle; ++i) {
			ParticleDescription newDescription = new ParticleDescription();
			newDescription.setX((float) (Math.random() * SIZE));
			newDescription.setY((float) (Math.random() * SIZE));
			newDescription.setMass(SIZE_DISPLAY);
			final long speed = (long) (Math.random() * 1.0);
			final int angle = (int) (Math.random() * 360.0);
			newDescription.setSpeedX((float) (speed * Math.cos(angle * 3.14 / 180.0)));
			newDescription.setSpeedY((float) (speed * Math.sin(angle * 3.14 / 180.0)));
			newDescription.setExist(true);
			result[i] = newDescription;
		}
		return result;
	}

	public static void main(final String[] args) {
		create();
		try {
			Thread.sleep(200L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < ITERATION; ++i) {
			updateEnvironment();
			try {
				Thread.sleep(STEP_DURATION);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			f.repaint();
		}
	}

	private static void updateEnvironment() {
		final float multiplyingFactor = 10.0f;

		final float repulsiveMatterAntiMatterFactor = MATTER_ANTIMATTER_REPULSION ? -1.0f : 1.0f;

		treatMatterToMatterCollisions();
		treatAntimatterToAntimatterCollision();
		treatMatterToAntimatterCollision();
		updateMatterSpeedAndPosition(multiplyingFactor, repulsiveMatterAntiMatterFactor);
		updateAntimatterSpeedAndPosition(multiplyingFactor, repulsiveMatterAntiMatterFactor);
	}

	private static void updateAntimatterSpeedAndPosition(final float multiplyingFactor,
			final float repulsiveMatterAntiMatterFactor) {
		for (int i = 0; i < NUMBER_OF_ANTIPARTICLES; ++i) {
			ParticleDescription aParticle = descriptionAntiparticle[i];
			if (!aParticle.exists()) {
				continue;
			}

			float x = aParticle.getX();
			float y = aParticle.getY();
			aParticle.setX(x + aParticle.getSpeedX());
			aParticle.setY(y + aParticle.getSpeedY());

			for (int j = 0; j < NUMBER_OF_PARTICLES; ++j) {
				ParticleDescription otherParticle = descriptionParticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);
				final double angle = Math.atan((otherParticle.getY() - y) / (otherParticle.getX() - x));
				final double xCorrectingFactor = otherParticle.getX() - x > 0 ? 1.0 : -1.0;
				final double yCorrectingFactor = otherParticle.getY() - y < 0 ? 1.0 : -1.0;

				aParticle.setSpeedX(
						aParticle.getSpeedX() + (float) (multiplyingFactor * xCorrectingFactor * otherParticle.getMass()
								* repulsiveMatterAntiMatterFactor * Math.cos(angle) / squaredDistance));
				aParticle.setSpeedY(aParticle.getSpeedY() + (float) (multiplyingFactor * otherParticle.getMass()
						* repulsiveMatterAntiMatterFactor * yCorrectingFactor * Math.sin(angle) / squaredDistance));
			}

			for (int j = 0; j < NUMBER_OF_ANTIPARTICLES; ++j) {
				ParticleDescription otherParticle = descriptionAntiparticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);
				final double angle = Math.atan((otherParticle.getY() - y) / (otherParticle.getX() - x));
				final double xCorrectingFactor = otherParticle.getX() - x > 0 ? 1.0 : -1.0;
				final double yCorrectingFactor = otherParticle.getY() - y < 0 ? 1.0 : -1.0;

				aParticle.setSpeedX(aParticle.getSpeedX() + (float) (multiplyingFactor * xCorrectingFactor
						* otherParticle.getMass() * Math.cos(angle) / squaredDistance));
				aParticle.setSpeedY(aParticle.getSpeedY() + (float) (multiplyingFactor * yCorrectingFactor
						* otherParticle.getMass() * Math.sin(angle) / squaredDistance));
			}
		}
	}

	private static void updateMatterSpeedAndPosition(final float multiplyingFactor,
			final float repulsiveMatterAntiMatterFactor) {
		for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
			ParticleDescription aParticle = descriptionParticle[i];
			if (!aParticle.exists()) {
				continue;
			}

			float x = aParticle.getX();
			float y = aParticle.getY();
			aParticle.setX(x + aParticle.getSpeedX());
			aParticle.setY(y + aParticle.getSpeedY());

			for (int j = 0; j < NUMBER_OF_PARTICLES; ++j) {
				ParticleDescription otherParticle = descriptionParticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);
				final double angle = Math.atan((otherParticle.getY() - y) / (otherParticle.getX() - x));
				final double xCorrectingFactor = otherParticle.getX() - x > 0 ? 1.0 : -1.0;
				final double yCorrectingFactor = otherParticle.getY() - y < 0 ? 1.0 : -1.0;

				aParticle.setSpeedX(aParticle.getSpeedX() + (float) (multiplyingFactor * otherParticle.getMass()
						* xCorrectingFactor * Math.cos(angle) / squaredDistance));
				aParticle.setSpeedY(aParticle.getSpeedY() + (float) (multiplyingFactor * yCorrectingFactor
						* otherParticle.getMass() * Math.sin(angle) / squaredDistance));
			}

			for (int j = 0; j < NUMBER_OF_ANTIPARTICLES; ++j) {
				ParticleDescription otherParticle = descriptionAntiparticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);
				final double angle = Math.atan((otherParticle.getY() - y) / (otherParticle.getX() - x));
				final double xCorrectingFactor = otherParticle.getX() - x > 0 ? 1.0 : -1.0;
				final double yCorrectingFactor = otherParticle.getY() - y < 0 ? 1.0 : -1.0;

				aParticle.setSpeedX(
						aParticle.getSpeedX() + (float) (multiplyingFactor * xCorrectingFactor * otherParticle.getMass()
								* repulsiveMatterAntiMatterFactor * Math.cos(angle) / squaredDistance));
				aParticle.setSpeedY(aParticle.getSpeedY() + (float) (multiplyingFactor * otherParticle.getMass()
						* repulsiveMatterAntiMatterFactor * yCorrectingFactor * Math.sin(angle) / squaredDistance));
			}
		}
	}

	private static void treatMatterToAntimatterCollision() {
		for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
			ParticleDescription aParticle = descriptionParticle[i];
			if (!aParticle.exists()) {
				continue;
			}

			float x = aParticle.getX();
			float y = aParticle.getY();

			for (int j = 0; j < NUMBER_OF_ANTIPARTICLES; ++j) {
				ParticleDescription otherParticle = descriptionAntiparticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);

				if (squaredDistance <= SIZE_DISPLAY * (aParticle.getMass() + otherParticle.getMass())) {
					ParticleDescription heavierParticle, lighterParticle;
					if (aParticle.getMass() >= otherParticle.getMass()) {
						heavierParticle = aParticle;
						lighterParticle = otherParticle;
					} else {
						heavierParticle = otherParticle;
						lighterParticle = aParticle;
					}
					
					heavierParticle.setX((heavierParticle.getX() + lighterParticle.getX()) / 2.0f);
					heavierParticle.setY((heavierParticle.getY() + lighterParticle.getY()) / 2.0f);
					heavierParticle.setMass(heavierParticle.getMass() - lighterParticle.getMass());

					final long speed = (long) (Math.random() * 1.0);
					final int angle = (int) (Math.random() * 360.0);
					heavierParticle.setSpeedX((float) (speed * Math.cos(angle * 3.14 / 180.0)));
					heavierParticle.setSpeedY((float) (speed * Math.sin(angle * 3.14 / 180.0)));

					heavierParticle.setExist(heavierParticle.getMass() > 0);
					lighterParticle.setExist(false);
				}
			}
		}
	}

	private static void treatAntimatterToAntimatterCollision() {
		for (int i = 0; i < NUMBER_OF_ANTIPARTICLES; ++i) {
			ParticleDescription aParticle = descriptionAntiparticle[i];
			if (!aParticle.exists()) {
				continue;
			}

			for (int j = 0; j < NUMBER_OF_ANTIPARTICLES; ++j) {

				ParticleDescription otherParticle = descriptionAntiparticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				float x = aParticle.getX();
				float y = aParticle.getY();

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);

				if (squaredDistance <= SIZE_DISPLAY * aParticle.getMass() + SIZE_DISPLAY * otherParticle.getMass()) {
					aParticle.setX((x + otherParticle.getX()) / 2.0f);
					aParticle.setY((y + otherParticle.getY()) / 2.0f);
					aParticle.setMass(aParticle.getMass() + otherParticle.getMass());

					final long speed = (long) (Math.random() * 1.0);
					final int angle = (int) (Math.random() * 360.0);
					aParticle.setSpeedX((float) (speed * Math.cos(angle * 3.14 / 180.0)));
					aParticle.setSpeedY((float) (speed * Math.sin(angle * 3.14 / 180.0)));

					otherParticle.setExist(false);
				}
			}
		}
	}

	private static void treatMatterToMatterCollisions() {
		for (int i = 0; i < NUMBER_OF_PARTICLES; ++i) {
			ParticleDescription aParticle = descriptionParticle[i];
			if (!aParticle.exists()) {
				continue;
			}

			for (int j = 0; j < NUMBER_OF_PARTICLES; ++j) {

				ParticleDescription otherParticle = descriptionParticle[j];
				if (i == j || !otherParticle.exists()) {
					continue;
				}

				float x = aParticle.getX();
				float y = aParticle.getY();

				final double squaredDistance = Math.pow(x - otherParticle.getX(), 2.0)
						+ Math.pow(y - otherParticle.getY(), 2.0);

				if (squaredDistance <= SIZE_DISPLAY * aParticle.getMass() + SIZE_DISPLAY * otherParticle.getMass()) {
					aParticle.setX((x + otherParticle.getX()) / 2.0f);
					aParticle.setY((y + otherParticle.getY()) / 2.0f);
					aParticle.setMass(aParticle.getMass() + otherParticle.getMass());

					final long speed = (long) (Math.random() * 1.0);
					final int angle = (int) (Math.random() * 360.0);
					aParticle.setSpeedX((float) (speed * Math.cos(angle * 3.14 / 180.0)));
					aParticle.setSpeedY((float) (speed * Math.sin(angle * 3.14 / 180.0)));

					otherParticle.setExist(false);
				}
			}
		}
	}
}