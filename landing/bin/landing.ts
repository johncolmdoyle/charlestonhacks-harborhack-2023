#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { LandingStack } from '../lib/landing-stack';

const app = new cdk.App();
new LandingStack(app, 'LandingStack', {
  env: { account: '435481488684', region: 'us-east-1' },
});