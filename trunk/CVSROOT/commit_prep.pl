#!/usr/bin/perl
#
# Perl filter to handle pre-commit checking of files.  This program
# records the last directory where commits will be taking place for
# use by the log_accum.pl script.
#
# Contributed by David Hampton <hampton@cisco.com>
# Stripped to minimum by Roy Fielding
#
############################################################
$TMPDIR        = $ENV{'TMPDIR'} || '/tmp';
$FILE_PREFIX   = '#cvs.';

$LAST_FILE     = "$TMPDIR/${FILE_PREFIX}lastdir"; # MUST match log_accum.pl

sub write_line {
    local($filename, $line) = @_;

    open(FILE, ">$filename") || die("Cannot open $filename: $!\n");
    print(FILE $line, "\n");
    close(FILE);
}

#
# Record this directory as the last one checked.  This will be used
# by the log_accumulate script to determine when it is processing
# the final directory of a multi-directory commit.
#
$id = getpgrp();

&write_line("$LAST_FILE.$id", $ARGV[0]);

exit(0);
