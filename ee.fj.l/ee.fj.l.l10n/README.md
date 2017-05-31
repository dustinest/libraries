# Simple event based localization library to ease up application developement

The purpose of this library is to ease up translation management and to change translations in desktop or any single user application.

## Usage

See [Junit tests](src/test/java/ee/fj/l/l10n/TranslationsFactoryTest.java) for basic use cases

Default language file is ``translation.properties``

Locale based transaltion files:

translation.et.properties or translation.et-EE.properties see more at ee.fj.l.l10n.CommonTranslationFileReader

Simple usage:

	Label label = new Label();
	TranslationFactory.translate("my.label.ley", "This is default label", t -> label.setText(t));

Change locale:

	TranslationFactory.setLocale(preferredlocale);

The translate above will be called and label changed

You might want to show variables in your translation:

	Translatable translatable = translate("my.label.ley", "The socre of ${name} is ${value}", t -> label.setText(t), "name", "value");

	translatable.translate("Joel", 1);

If locale or values are changed the label will be translated. In case the new translatable has variables in different order, for instance `"${value} - ${name}"` then the default order is respected. You can still call `translatable.translate("Joel", 1);`.

If you want to change where translations are read. Default is from filesystem properties files

	LocalizationFactory.set(TranslationFileReader reader);

If you want to change how translations are being read (default is properties file) 

	public static void set(TranslationReader reader);

## Installation

Add repository to your ``pom.xml``:

	<repositories>
		<repository>
			<id>ee.fj-mvn-repo</id>
			<url>https://raw.githubusercontent.com/dustinest/libraries/mvn-repo</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>

And dependency:

	<dependency>
		<groupId>ee.fj.l</groupId>
		<artifactId>ee.fj.l.l10n</artifactId>
		<version>0.0.1</version>
	</dependency>
